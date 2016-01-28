package com.vlabs.androiweartest.wear.model;

import com.vlabs.androiweartest.integration.InPort;
import com.vlabs.androiweartest.oughter.OuterPlayerState;
import com.vlabs.androiweartest.wear.connection.ConnectionManager;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.WearPlayerState;

import rx.functions.Action1;

public class PlayerStateChangedModel implements Action1<WearPlayerState> {

    private final ConnectionManager mConnectionManager;

    public PlayerStateChangedModel(final InPort.PlayerStateChangedPin<OuterPlayerState> playerStateChangedPin, final ConnectionManager connectionManager) {
        mConnectionManager = connectionManager;
        playerStateChangedPin.onInnerChanged().subscribe(this);
    }

    @Override
    public void call(final WearPlayerState wearPlayerState) {
        mConnectionManager.broadcastMessage(WearMessage.STATE, wearPlayerState.getData());
    }
}
