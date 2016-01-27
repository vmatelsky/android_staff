package com.vlabs.wearcontract.dummy;

import com.vlabs.DataMapBuilder;
import com.vlabs.wearcontract.WearPlayerState;

public class DummyWearPlayerState {

    public static WearPlayerState Dummy1 = new WearPlayerState(
            new DataMapBuilder()
                    .putBoolean(WearPlayerState.KEY_PLAYING, true)
                    .putString(WearPlayerState.KEY_IMAGE, DummyWearStation.Dummy1.getImagePath())
                    .putString(WearPlayerState.KEY_STATION_IMAGE, DummyWearStation.Dummy1.name())
                    .putString(WearPlayerState.KEY_TITLE, "Dummy1 title")
                    .putString(WearPlayerState.KEY_SUBTITLE, "subtitle dummy 1")
                    .putInt(WearPlayerState.KEY_DEVICE_VOLUME, 3)
                    .putBoolean(WearPlayerState.KEY_THUMBED, true)
                    .putBoolean(WearPlayerState.KEY_THUMBS_ENABLED, true)
                    .getMap());

    public static WearPlayerState Dummy2 = new WearPlayerState(
            new DataMapBuilder()
                    .putBoolean(WearPlayerState.KEY_PLAYING, true)
                    .putString(WearPlayerState.KEY_IMAGE, DummyWearStation.Dummy2.getImagePath())
                    .putString(WearPlayerState.KEY_STATION_IMAGE, DummyWearStation.Dummy2.name())
                    .putString(WearPlayerState.KEY_TITLE, "Dummy2 title")
                    .putString(WearPlayerState.KEY_SUBTITLE, "subtitle dummy 2")
                    .putInt(WearPlayerState.KEY_DEVICE_VOLUME, 3)
                    .putBoolean(WearPlayerState.KEY_THUMBED, true)
                    .putBoolean(WearPlayerState.KEY_THUMBS_ENABLED, true)
                    .getMap());
}
