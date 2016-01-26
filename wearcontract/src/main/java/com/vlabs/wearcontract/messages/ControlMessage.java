package com.vlabs.wearcontract.messages;

import android.support.annotation.NonNull;

import com.google.android.gms.wearable.DataMap;
import com.vlabs.DataMapBuilder;

public class ControlMessage {

    public enum ControlAction {
        PLAY("play"),
        STOP("stop"),
        THUMB_UP("thumb-up"),
        THUMB_DOWN("thumb-down"),
        VOLUME_UP("volume-up"),
        VOLUME_DOWN("volume-down");

        private final String mPath;

        ControlAction(final String path) {
            mPath = path;
        }

        public String asString() {
            return mPath;
        }
    }

    public static final String KEY_ACTION = "action";

    private final ControlAction mControlAction;

    public ControlMessage(final ControlAction controlAction) {
        mControlAction = controlAction;
    }

    public ControlMessage(@NonNull final DataMap dataMap) {
        final String actionString = dataMap.getString(KEY_ACTION);

        for (ControlAction action : ControlAction.values()) {
            if (actionString.equals(action.asString())) {
                mControlAction = action;
                return;
            }
        }

        throw new RuntimeException("Unknown string for action: " + actionString);
    }

    public ControlAction asPlayerState() {
        return mControlAction;
    }

    public DataMap asDataMap() {
        return new DataMapBuilder()
                .putString(KEY_ACTION, mControlAction.asString())
                .getMap();
    }
}
