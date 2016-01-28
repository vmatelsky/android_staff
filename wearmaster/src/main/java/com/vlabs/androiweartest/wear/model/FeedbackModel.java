package com.vlabs.androiweartest.wear.model;

import com.vlabs.androiweartest.integration.InPort;
import com.vlabs.androiweartest.wear.connection.ConnectionManager;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.messages.FeedbackMessage;
import com.vlabs.wearcontract.model.Feedback;

import rx.functions.Action1;

public class FeedbackModel implements Action1<Feedback> {

    private final ConnectionManager mConnectionManager;

    public FeedbackModel(final InPort.FeedbackPin feedbackPin, final ConnectionManager connectionManager) {
        mConnectionManager = connectionManager;
        feedbackPin.onInnerChanged().subscribe(this);
    }

    @Override
    public void call(final Feedback feedback) {
        mConnectionManager.broadcastMessage(WearMessage.FEEDBACK, new FeedbackMessage(feedback).asDataMap());
    }
}
