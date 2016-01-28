package com.vlabs.androiweartest;

import android.content.Context;
import android.widget.Toast;

import com.vlabs.wearcontract.messages.FeedbackMessage;

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
    public void onEventMainThread(FeedbackMessage event) {
        Toast.makeText(mContext, event.feedback().message(), Toast.LENGTH_LONG).show();
    }
}
