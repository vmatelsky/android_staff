package com.vlabs.androiweartest.images;

import android.content.res.Resources;

import com.path.android.jobqueue.JobManager;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.events.data.OnAssetLoaded;
import com.vlabs.androiweartest.job.AssetAsBitmap;
import com.vlabs.androiweartest.job.BroadcastMessageJob;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.messages.LoadImageMessage;

import de.greenrobot.event.EventBus;

public class ImageLoader {

    final JobManager mJobManager;

    final EventBus mEventBus;

    private final int mWindowHeight;
    private final int mWindowWidth;

    public ImageLoader(final JobManager jobManager, final EventBus eventBus) {
        mJobManager = jobManager;
        mEventBus = eventBus;
        mEventBus.register(this);

        final Resources res = WearApplication.instance().getResources();
        mWindowHeight = res.getDisplayMetrics().heightPixels;
        mWindowWidth = res.getDisplayMetrics().widthPixels;
    }

    public void imageByPath(final String imagePath) {
        mJobManager.addJobInBackground(new BroadcastMessageJob(WearMessage.LOAD_IMAGE, new LoadImageMessage(imagePath, mWindowHeight, mWindowWidth).asDataMap().toBundle()));
    }

    @SuppressWarnings("unused")
    public void onEventBackgroundThread(OnAssetLoaded event) {
        mJobManager.addJobInBackground(new AssetAsBitmap(event.path(), event.asset()));
    }

}
