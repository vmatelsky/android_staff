package com.vlabs.wearcontract.messages;

import com.google.android.gms.wearable.DataMap;
import com.vlabs.DataMapBuilder;

public class FeedbackMessage {

    public static final String KEY_MESSAGE = "message";

    private final String mFeedback;

    public FeedbackMessage(final String feedback) {
        mFeedback = feedback;
    }

    public FeedbackMessage(final DataMap dataMap) {
        mFeedback = dataMap.getString(KEY_MESSAGE);
    }

    public String feedback() {
        return mFeedback;
    }

    public DataMap asDataMap() {
        return new DataMapBuilder().putString(KEY_MESSAGE, mFeedback).getMap();
    }
}
