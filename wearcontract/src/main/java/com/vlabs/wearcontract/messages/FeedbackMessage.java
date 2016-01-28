package com.vlabs.wearcontract.messages;

import com.google.android.gms.wearable.DataMap;
import com.vlabs.DataMapBuilder;
import com.vlabs.wearcontract.model.Feedback;

public class FeedbackMessage {

    public static final String KEY_MESSAGE = "message";

    private final Feedback mFeedback;

    public FeedbackMessage(final Feedback feedback) {
        mFeedback = feedback;
    }

    public FeedbackMessage(final DataMap dataMap) {
        mFeedback = new Feedback(dataMap.getString(KEY_MESSAGE));
    }

    public Feedback feedback() {
        return mFeedback;
    }

    public DataMap asDataMap() {
        return new DataMapBuilder().putString(KEY_MESSAGE, mFeedback.message()).getMap();
    }
}
