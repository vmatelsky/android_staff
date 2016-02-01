package com.vlabs.androiweartest.behavior;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;

import com.clearchannel.iheartradio.controller.view.ImageByDataPathView;
import com.google.android.gms.wearable.DataMap;
import com.path.android.jobqueue.JobManager;
import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.events.data.OnDataReceived;
import com.vlabs.androiweartest.job.GetDataItems;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.messages.StateMessage;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class ChangeBackgroundBehavior {

    private final Context mContext;
    private final JobManager mJobManager;

    private final ArrayList<ImageByDataPathView> mActiveImages = new ArrayList<>();

    private boolean mLastIsPlayedState = false;
    private String mLastImagePath = null;

    public ChangeBackgroundBehavior(
            final Context context,
            final EventBus eventBus,
            final JobManager jobManager) {
        mContext = context;
        mJobManager = jobManager;
        eventBus.register(this);
    }

    public void activateFor(final ImageByDataPathView image) {
        image.setImagePath(mLastImagePath);

        if (!mActiveImages.contains(image)) {
            mActiveImages.add(image);
        }
    }

    public void deactivateFor(final ImageByDataPathView image) {
        mActiveImages.remove(image);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final StateMessage event) {
        final WearPlayerState state = event.asPlayerState();

        mLastIsPlayedState = state.isPlaying();

        if (mLastIsPlayedState) {
            updateImagePath(state.getImagePath());
        } else {
            setImageFromRecentStations();
        }
    }

    private void updateImagePath(final String imagePath) {
        mLastImagePath = imagePath;
        for (ImageByDataPathView image : mActiveImages) {
            image.setImagePath(imagePath);
        }
    }

    private void setImageFromRecentStations() {
        mJobManager.addJobInBackground(new GetDataItems(WearDataEvent.PATH_STATIONS_RECENT));
    }

    private void setDefaultBackgroundColor() {
        final int color = ContextCompat.getColor(mContext, R.color.notification_background);
        final ColorDrawable colorDrawable = new ColorDrawable(color);

        for (ImageByDataPathView image : mActiveImages) {
            image.setBackground(colorDrawable);
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(OnDataReceived event) {
        if (mLastIsPlayedState) return;

        final DataMap dataMap = event.dataMap();
        if (dataMap == null) return;

        final ArrayList<DataMap> stationMapLists = dataMap.getDataMapArrayList(WearDataEvent.KEY_STATIONS);

        if (stationMapLists.isEmpty()) {
            setDefaultBackgroundColor();
        } else {
            final WearStation mostRecentStation = WearStation.fromDataMap(stationMapLists.get(0));
            updateImagePath(mostRecentStation.getImagePath());

        }
    }
}
