package com.vlabs.androiweartest.events.message;

import android.util.Log;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.wearcontract.Message;

public class OnPlayerAction {

    public enum PlayerAction {
        Stop, Play, ThumbsDown, ThumbsUp, VolumeUp, VolumeDown, Unknown;

        static PlayerAction fromPlayerAction(final String action) {

            switch (action) {
                case Message.CONTROL_ACTION_STOP:
                    return Stop;
                case Message.CONTROL_ACTION_PLAY:
                    return Play;
                case Message.CONTROL_ACTION_THUMB_DOWN:
                    return ThumbsDown;
                case Message.CONTROL_ACTION_THUMB_UP:
                    return ThumbsUp;
                case Message.CONTROL_ACTION_VOLUME_UP:
                    return VolumeUp;
                case Message.CONTROL_ACTION_VOLUME_DOWN:
                    return VolumeDown;
                default:
                    Log.d(PlayerAction.class.getSimpleName(), "Unknown remote control action: " + action);
                    return Unknown;
            }
        }
    }

    public static OnPlayerAction fromMessageEvent(final MessageEvent event) {
        final DataMap map = DataMap.fromByteArray(event.getData());
        final String action = map.getString(Message.KEY_ACTION);
        return new OnPlayerAction(PlayerAction.fromPlayerAction(action));
    }

    private final PlayerAction mPlayerAction;

    public OnPlayerAction(final PlayerAction playerAction) {
        mPlayerAction = playerAction;
    }

    public PlayerAction playerAction() {
        return mPlayerAction;
    }

}
