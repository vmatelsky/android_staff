package com.vlabs.androiweartest.events.data;

import android.util.Log;

import com.google.android.gms.wearable.DataEvent;

public enum EventType {
    CHANGED, DELETED, GET, UNKNOWN;

    public static EventType fromDataEventType(final int value) {
        if (DataEvent.TYPE_CHANGED == value) {
            return CHANGED;
        } else if (DataEvent.TYPE_DELETED == value) {
            return DELETED;
        }

        Log.d(EventType.class.getSimpleName(), "Unknown data event type: " + value);
        return UNKNOWN;
    }
}
