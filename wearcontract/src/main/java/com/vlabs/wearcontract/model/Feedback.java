package com.vlabs.wearcontract.model;

public class Feedback {

    private final String mMessage;

    public Feedback(final String message) {
        mMessage = message;
    }

    public String message() {
        return mMessage;
    }
}
