package com.vlabs.androiweartest.integration;

import com.vlabs.wearcontract.WearStation;

import rx.Observable;

public class OutPort<WearType> {

    public static class StationPlayedPort extends OutPort<WearStation> {

        public StationPlayedPort(final Observable<WearStation> onChanged) {
            super(onChanged);
        }
    }

    private final Observable<WearType> mInnerChanged;

    public OutPort(Observable<WearType> onChanged) {
        mInnerChanged = onChanged;
    }

    public Observable<WearType> onChanged() {
        return mInnerChanged;
    }
}
