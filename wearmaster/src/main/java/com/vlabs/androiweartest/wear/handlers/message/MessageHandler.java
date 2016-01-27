package com.vlabs.androiweartest.wear.handlers.message;

import com.google.android.gms.wearable.MessageEvent;

public interface MessageHandler {

    void handle(MessageEvent messageEvent);

}
