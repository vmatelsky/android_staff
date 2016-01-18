package com.vlabs.androiweartest.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.activity.BaseActivity;
import com.vlabs.androiweartest.helpers.DataMapBuilder;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.models.StationListModel;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.Message;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearExtras;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.message.MessageManager;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

public class SearchActivity extends BaseActivity implements Action1<List<WearStation>> {

    @Inject
    Analytics mAnalytics;

    @Inject
    ConnectionManager mConnectionManager;

    @Inject
    MessageManager mMessageManager;


    private StationListModel mModel;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!wasLaunchedWithQuery()) {
            showToast("No query is passed");
            finish();
            return;
        }

        setContentView(R.layout.activity_search);

        getComponent().inject(this);

        mModel = new StationListModel(mMessageManager, mConnectionManager, Data.PATH_STATIONS_SEARCH);
        mModel.onStationsChanged().subscribe(this);
        searchFor(getIntent().getStringExtra(WearExtras.EXTRA_QUERY));

        if (savedInstanceState == null) {
            tagRemoteControl();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mModel.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mModel.stopListening();
    }

    public void showToast(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void searchFor(final String term) {
        mConnectionManager.broadcastMessage(
                Message.PATH_SEARCH,
                new DataMapBuilder().putString(Message.KEY_SEARCH_TERM, term).getMap());
    }

    private boolean wasLaunchedWithQuery() {
        return getIntent().hasExtra(WearExtras.EXTRA_QUERY);
    }

    void tagRemoteControl() {
        mAnalytics.broadcastRemoteControl(WearAnalyticsConstants.WearBrowse.VOICE_SEARCH_SYSTEM);
    }

    @Override
    public void call(final List<WearStation> wearStations) {
        if (wearStations.isEmpty()) {
            showToast(getString(R.string.search_no_result));
            finish();
            return;
        }

        final Intent intent = new Intent(SearchActivity.this, PlayStationActivity.class);
        intent.putExtra(WearExtras.EXTRA_STATION_LIST_PATH, Data.PATH_STATIONS_SEARCH);
        intent.putExtra(WearExtras.EXTRA_TITLE, getString(R.string.search_result_title));
        startActivity(intent);
        finish();
    }
}
