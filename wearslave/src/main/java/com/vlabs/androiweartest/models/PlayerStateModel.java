package com.vlabs.androiweartest.models;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.wearcontract.Message;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearmanagers.Receiver;
import com.vlabs.wearmanagers.message.MessageManager;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class PlayerStateModel {

    private final MessageManager mMessageManager;
    private final PlayerManager mPlayerManager;
    private Subscription mCurrentSubscription;

    private final PublishSubject<WearPlayerState> mOnPlayerStateChanged = PublishSubject.create();

    private Action1<MessageEvent> mOnStateChanged = new Action1<MessageEvent>() {
        @Override
        public void call(final MessageEvent message) {

            final DataMap map = DataMap.fromByteArray(message.getData());
            final WearPlayerState state = new WearPlayerState(map);
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

    public void startListening() {
        mCurrentSubscription = mMessageManager.onMessageByPath(Message.PATH_STATE).subscribe(mOnStateChanged);
    }

    public void stopListening() {
        if (mCurrentSubscription != null) {
            mCurrentSubscription.unsubscribe();
            mCurrentSubscription = null;
        }
    }

    public WearPlayerState getState() {
        return mPlayerManager.currentState();
    }

}
