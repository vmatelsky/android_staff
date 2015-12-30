package com.vlabs.androiweartest.models;

import com.vlabs.wearcontract.Message;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearmanagers.Receiver;
import com.vlabs.wearmanagers.message.MessageManager;

import java.util.ArrayList;
import java.util.List;

public class PlayerStateModel {

    private final MessageManager mMessageManager;
    private final PlayerManager mPlayerManager;
    private final List<Receiver<WearPlayerState>> mPlayerStateChangeListeners = new ArrayList<>();
    private Receiver<MessageManager.Message> mOnStateChanged = new Receiver<MessageManager.Message>() {
        @Override
        public void receive(final MessageManager.Message message) {
            final WearPlayerState state = new WearPlayerState(message.dataMap());

            for (Receiver<WearPlayerState> receiver : mPlayerStateChangeListeners) {
                receiver.receive(state);
            }
        }
    };

    public PlayerStateModel(final MessageManager messageManager, final PlayerManager playerManager) {
        mMessageManager = messageManager;
        mPlayerManager = playerManager;
    }

    public void startListening() {
        mMessageManager.subscribeOnMessage(Message.PATH_STATE, mOnStateChanged);
    }

    public void stopListening() {
        mMessageManager.subscribeOnMessage(Message.PATH_STATE, mOnStateChanged);
    }

    public void addPlayerStateChangeListener(Receiver<WearPlayerState> listener) {
        mPlayerStateChangeListeners.add(listener);
    }

    public void removePlayerStateChangeListener(Receiver<WearPlayerState> listener) {
        mPlayerStateChangeListeners.remove(listener);
    }

    public WearPlayerState getState() {
        return mPlayerManager.currentState();
    }

}
