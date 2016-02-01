package com.vlabs.androiweartest.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;
import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.activity.BaseActivity;
import com.vlabs.androiweartest.events.data.OnStations;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.job.BroadcastMessageJob;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearExtras;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.dataevent.EventType;
import com.vlabs.wearcontract.messages.SearchMessage;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class SearchActivity extends BaseActivity {

    @Inject
    Analytics mAnalytics;

    @Inject
    JobManager mJobManager;

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
        mJobManager.addJobInBackground(new BroadcastMessageJob(WearMessage.SEARCH, new SearchMessage(term).asDataMap().toBundle()));
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

        if (event.path().equals(WearDataEvent.PATH_STATIONS_SEARCH)) {

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
            intent.putExtra(WearExtras.EXTRA_STATION_LIST_PATH, WearDataEvent.PATH_STATIONS_SEARCH);
            intent.putExtra(WearExtras.EXTRA_TITLE, getString(R.string.search_result_title));
            startActivity(intent);
            finish();
        }
    }
}
