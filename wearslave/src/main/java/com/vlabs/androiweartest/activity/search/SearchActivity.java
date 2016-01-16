package com.vlabs.androiweartest.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.Toast;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.helpers.DataMapBuilder;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.models.StationListModel;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.Message;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearExtras;
import com.vlabs.wearcontract.WearStation;

import java.util.List;

import rx.functions.Action1;

public class SearchActivity extends WearableActivity implements Action1<List<WearStation>> {

    private static final int REQUEST_PLAY = 2;

    private Analytics mAnalytics;
    private StationListModel mModel;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (!wasLaunchedWithQuery()) {
            showToast("No query is passed");
            finish();
            return;
        }

        mAnalytics = WearApplication.instance().analytics();
        mModel = new StationListModel(WearApplication.instance().messageManager(), Data.PATH_STATIONS_SEARCH);
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
        WearApplication.instance().connectionManager().broadcastMessage(
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
