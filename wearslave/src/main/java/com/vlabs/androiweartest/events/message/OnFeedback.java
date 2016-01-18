package com.vlabs.androiweartest.events.message;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.wearcontract.Message;

public class OnFeedback {

    public static OnFeedback fromMessageEvent(final MessageEvent event) {
        final DataMap map = DataMap.fromByteArray(event.getData());
        final String message = map.getString(Message.KEY_MESSAGE);

        return new OnFeedback(message);
    }

    private final String mMessage;

    public OnFeedback(final String message) {
        mMessage = message;
    }

    public String message() {
        return mMessage;
    }
}
