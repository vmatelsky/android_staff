package com.vlabs.androiweartest;

import android.content.Context;
import android.widget.Toast;

import com.vlabs.androiweartest.events.message.OnFeedback;

import de.greenrobot.event.EventBus;

public class CommonMessagesHandler {

    private final Context mContext;
    private final EventBus mEventBus;

    public CommonMessagesHandler(
            final Context context,
            final EventBus eventBus) {
        mContext = context;
        mEventBus = eventBus;
    }

    public void invoke() {
        mEventBus.register(this);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(OnFeedback event) {
        Toast.makeText(mContext, event.message(), Toast.LENGTH_LONG).show();
    }
}
