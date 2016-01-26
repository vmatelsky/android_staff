package com.vlabs.wearcontract;

import com.google.android.gms.wearable.DataMap;
import com.vlabs.wearcontract.messages.AnalyticsMessage;
import com.vlabs.wearcontract.messages.ControlMessage;
import com.vlabs.wearcontract.messages.DismissNotificationMessage;
import com.vlabs.wearcontract.messages.FeedbackMessage;
import com.vlabs.wearcontract.messages.LoadImageMessage;
import com.vlabs.wearcontract.messages.PlayStationMessage;
import com.vlabs.wearcontract.messages.ReportCrashMessage;
import com.vlabs.wearcontract.messages.SearchMessage;
import com.vlabs.wearcontract.messages.StateMessage;

public enum WearMessage {
    STATE("/state") {
        @Override
        public Object toDomain(final DataMap dataMap) {
            return new StateMessage(dataMap);
        }
    },
    CONTROL("/control") {
        @Override
        public Object toDomain(final DataMap dataMap) {
            return new ControlMessage(dataMap);
        }
    },
    FEEDBACK("/feedback") {
        @Override
        public Object toDomain(final DataMap dataMap) {
            return new FeedbackMessage(dataMap);
        }
    },
    SEARCH("/search") {
        @Override
        public Object toDomain(final DataMap dataMap) {
            return new SearchMessage(dataMap);
        }
    },
    CRASH_REPORTS("/crashes") {
        @Override
        public Object toDomain(final DataMap dataMap) {
            return new ReportCrashMessage(dataMap);
        }
    },
    ANALYTICS("/analytics") {
        @Override
        public Object toDomain(final DataMap dataMap) {
            return new AnalyticsMessage(dataMap);
        }
    },
    PLAY_STATION("/play") {
        @Override
        public Object toDomain(final DataMap dataMap) {
            return new PlayStationMessage(dataMap);
        }
    },
    LOAD_IMAGE("/load-image") {
        @Override
        public Object toDomain(final DataMap dataMap) {
            return new LoadImageMessage(dataMap);
        }
    },
    DISMISS_NOTIFICATION("/dismiss_notification") {
        @Override
        public Object toDomain(final DataMap dataMap) {
            return new DismissNotificationMessage(dataMap);
        }
    };

    private final String mPath;

    WearMessage(final String path) {
        mPath = path;
    }

    public String path() {
        return mPath;
    }

    public abstract Object toDomain(final DataMap dataMap);

}