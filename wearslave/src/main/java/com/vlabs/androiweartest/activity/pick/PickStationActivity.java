package com.vlabs.androiweartest.activity.pick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.activity.BaseActivity;
import com.vlabs.androiweartest.activity.pick.adapters.AdapterFactory;
import com.vlabs.androiweartest.activity.pick.adapters.ClickableAdapter;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.models.StationListModel;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearExtras;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.message.MessageManager;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

public class PickStationActivity extends BaseActivity implements Action1<List<WearStation>> {

    private enum ListType {
        FOR_YOU,
        MY_STATIONS,
    }

    public static Intent createIntent(final Activity activity, final String extraStationPath, final String extraNoStationsMessage) {
        final Intent intent = new Intent(activity, PickStationActivity.class);
        intent.putExtra(WearExtras.EXTRA_STATION_LIST_PATH, extraStationPath);
        intent.putExtra(WearExtras.EXTRA_NO_STATIONS_MESSAGE, extraNoStationsMessage);
        return intent;
    }

    @Inject
    Analytics mAnalytics;

    @Inject
    MessageManager mMessageManager;

    @Inject
    ConnectionManager mConnectionManager;

    private WearableListView mStationList;
    private TextView mEmptyMessageView;
    private WearableListView.Adapter mAdapter;
    private AdapterFactory mAdapterFactory;
    private StationListModel mModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().inject(this);

        setContentView(R.layout.activity_pick_station);

        mStationList = (WearableListView) findViewById(R.id.station_list);
        mEmptyMessageView = (TextView) findViewById(R.id.empty_message);

        mModel = new StationListModel(mMessageManager, mConnectionManager, stationsListPath());
        mModel.onStationsChanged().subscribe(this);

        mAdapterFactory = new AdapterFactory(this);
        mStationList.setClickListener(onItemClickedListener);

        if (savedInstanceState == null) {
            tagRemoteControl();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mModel.startListening();
        mModel.refresh();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mModel.stopListening();
    }

    @Override
    public void call(final List<WearStation> wearStations) {
        if (isFinishing()) return;

        if (wearStations.isEmpty()) {
            mEmptyMessageView.setText(getIntent().getStringExtra(WearExtras.EXTRA_NO_STATIONS_MESSAGE));
            mStationList.setVisibility(View.GONE);
            mEmptyMessageView.setVisibility(View.VISIBLE);
        } else {
            mStationList.setVisibility(View.VISIBLE);
            mEmptyMessageView.setVisibility(View.GONE);

            final ListType listType = getListType();
            if (listType == ListType.FOR_YOU) {
                mAdapter = mAdapterFactory.createForYouAdapter(wearStations);
                setAdapterAndScroll();
            } else if (listType == ListType.MY_STATIONS) {
                mAdapter = mAdapterFactory.createMyStationsAdapter(wearStations);
                setAdapterAndScroll();
            }
        }
    }

    private final WearableListView.ClickListener onItemClickedListener = new WearableListView.ClickListener() {
        @Override
        public void onClick(final WearableListView.ViewHolder viewHolder) {
            if (mAdapter instanceof ClickableAdapter) {
                ((ClickableAdapter) mAdapter).handleClick(PickStationActivity.this, viewHolder.getPosition());
            }
        }

        @Override
        public void onTopEmptyRegionClick() {
            finishAffinity();
        }
    };

    private void setAdapterAndScroll() {
        runOnUiThread(() -> {
            mStationList.setAdapter(mAdapter);
            mStationList.smoothScrollToPosition(0);
        });
    }

    void tagRemoteControl() {
        final ListType listType = getListType();

        if (listType == ListType.FOR_YOU) {
            mAnalytics.broadcastRemoteControl(WearAnalyticsConstants.WearBrowse.FOR_YOU);
        } else if (listType == ListType.MY_STATIONS) {
            mAnalytics.broadcastRemoteControl(WearAnalyticsConstants.WearBrowse.MY_STATIONS);
        }
    }

    private ListType getListType() {
        final String path = getIntent().getStringExtra(WearExtras.EXTRA_STATION_LIST_PATH);

        if (Data.PATH_STATIONS_FOR_YOU.equals(path)) {
            return ListType.FOR_YOU;
        } else if (Data.PATH_STATIONS_MY_STATIONS.equals(path)) {
            return ListType.MY_STATIONS;
        } else {
            return null;
        }
    }

    private String stationsListPath() {
        final String path = getIntent().getStringExtra(WearExtras.EXTRA_STATION_LIST_PATH);
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("No path specified, please specify a path to listen for station list changes via EXTRA_STATION_LIST_PATH");
        }

        return path;
    }

}
