package com.vlabs.androiweartest.models;

import com.vlabs.wearcontract.Message;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearmanagers.Receiver;
import com.vlabs.wearmanagers.message.MessageManager;

import rx.Observable;
import rx.subjects.PublishSubject;

public class PlayerStateModel {

    private final MessageManager mMessageManager;
    private final PlayerManager mPlayerManager;

    private final PublishSubject<WearPlayerState> mOnPlayerStateChanged = PublishSubject.create();

    private Receiver<MessageManager.Message> mOnStateChanged = new Receiver<MessageManager.Message>() {
        @Override
        public void receive(final MessageManager.Message message) {
            final WearPlayerState state = new WearPlayerState(message.dataMap());
            mOnPlayerStateChanged.onNext(state);
        }
    };

    public PlayerStateModel(final MessageManager messageManager, final PlayerManager playerManager) {
        mMessageManager = messageManager;
        mPlayerManager = playerManager;
    }

    public Observable<WearPlayerState> onPlayerStateChanged() {
        return mOnPlayerStateChanged;
    }

//    public void startListening() {
//        mMessageManager.subscribeOnMessage(Message.PATH_STATE, mOnStateChanged);
//    }
//
//    public void stopListening() {
//        mMessageManager.subscribeOnMessage(Message.PATH_STATE, mOnStateChanged);
//    }

    public WearPlayerState getState() {
        return mPlayerManager.currentState();
    }

}
