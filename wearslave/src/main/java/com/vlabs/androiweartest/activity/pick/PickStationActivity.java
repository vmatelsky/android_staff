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
import com.vlabs.androiweartest.events.data.OnStations;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.manager.ConnectionManager;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearExtras;
import com.vlabs.wearcontract.WearStation;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class PickStationActivity extends BaseActivity {

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
    ConnectionManager mConnectionManager;

    @Inject
    EventBus mEventBus;

    private WearableListView mStationList;
    private TextView mEmptyMessageView;
    private WearableListView.Adapter mAdapter;
    private AdapterFactory mAdapterFactory;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().inject(this);

        setContentView(R.layout.activity_pick_station);

        mStationList = (WearableListView) findViewById(R.id.station_list);
        mEmptyMessageView = (TextView) findViewById(R.id.empty_message);

        mAdapterFactory = new AdapterFactory(this);
        mStationList.setClickListener(onItemClickedListener);

        if (savedInstanceState == null) {
            tagRemoteControl();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEventBus.register(this);
        refresh();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mEventBus.unregister(this);
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

        if (WearDataEvent.PATH_STATIONS_FOR_YOU.equals(path)) {
            return ListType.FOR_YOU;
        } else if (WearDataEvent.PATH_STATIONS_MY_STATIONS.equals(path)) {
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

    @SuppressWarnings("unused")
    public void onEventMainThread(OnStations event) {
        if (isFinishing()) return;

        if (event.path().equals(stationsListPath())) {
            final List<WearStation> wearStations = event.stations();
            if (wearStations == null || wearStations.isEmpty()) {
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
    }

    public void refresh() {
        mConnectionManager.getDataItems(stationsListPath(), (path, map) -> onEventMainThread(OnStations.fromDataMap(map, path)));
    }

}
