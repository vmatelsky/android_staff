package com.vlabs.androiweartest.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.activity.BaseActivity;
import com.vlabs.androiweartest.events.data.EventType;
import com.vlabs.androiweartest.events.data.OnStations;
import com.vlabs.DataMapBuilder;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.Message;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearExtras;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.message.MessageManager;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class SearchActivity extends BaseActivity {

    @Inject
    Analytics mAnalytics;

    @Inject
    ConnectionManager mConnectionManager;

    @Inject
    MessageManager mMessageManager;

    @Inject
    EventBus mEventBus;

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

        searchFor(getIntent().getStringExtra(WearExtras.EXTRA_QUERY));

        if (savedInstanceState == null) {
            tagRemoteControl();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mEventBus.unregister(this);
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

    @SuppressWarnings("unused")
    public void onEventMainThread(OnStations event) {
        if (isFinishing()) return;

        if (event.path().equals(Data.PATH_STATIONS_SEARCH)) {

            if (event.eventType() != EventType.CHANGED) {
                return;
            }

            final List<WearStation> wearStations = event.stations();
            if (wearStations == null || wearStations.isEmpty()) {
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
}
