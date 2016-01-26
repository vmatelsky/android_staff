package com.vlabs.androiweartest.behavior;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;

import com.clearchannel.iheartradio.controller.view.ImageByDataPathView;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.manager.ConnectionManager;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.messages.StateMessage;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class ChangeBackgroundBehavior {

    private final EventBus mEventBus;
    private final ConnectionManager mConnectionManager;
    private ImageByDataPathView mImage;

    private boolean mLastIsPlayedState = false;

    public ChangeBackgroundBehavior(final EventBus eventBus, final ConnectionManager connectionManager) {
        mEventBus = eventBus;
        mConnectionManager = connectionManager;
    }

    public void activate(final ImageByDataPathView image) {
        mImage = image;
        mEventBus.register(this);
    }

    public void deactivate() {
        mEventBus.unregister(this);
        mImage = null;
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final StateMessage event) {
        WearPlayerState state = event.asPlayerState();

        mLastIsPlayedState = state.isPlaying();

        if (state.isPlaying()) {
            mImage.setImagePath(state.getImagePath());
        } else {
            setImageFromRecentStations();
        }
    }

    private void setImageFromRecentStations() {
        mConnectionManager.getDataItems(WearDataEvent.PATH_STATIONS_RECENT, (path, map) -> {

            if (mLastIsPlayedState) return;

            if (map == null) return;

            final ArrayList<DataMap> stationMapLists = map.getDataMapArrayList(WearDataEvent.KEY_STATIONS);

            if (stationMapLists.isEmpty()) {
                setDefaultBackgroundColor();
            } else {
                final WearStation mostRecentStation = WearStation.fromDataMap(stationMapLists.get(0));
                mImage.setImagePath(mostRecentStation.getImagePath());

            }
        });
    }

    private void setDefaultBackgroundColor() {
        final int color = ContextCompat.getColor(mImage.getContext(), R.color.notification_background);
        final ColorDrawable colorDrawable = new ColorDrawable(color);
        mImage.setBackground(colorDrawable);
    }
}
