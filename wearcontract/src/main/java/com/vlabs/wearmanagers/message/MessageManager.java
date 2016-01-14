package com.vlabs.wearmanagers.message;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;

import rx.Observable;

public interface MessageManager {

    class Message {

        private final String mPath;
        private final DataMap mReceiver;

        public Message(final String path, final DataMap map) {
            mPath = path;
            mReceiver = map;
        }

        public String path() {
            return mPath;
        }

        public DataMap dataMap() {
            return mReceiver;
        }
    }

    Observable<MessageEvent> onMessageByPath(final String path);
    Observable<DataEvent> onDataByPath(final String path);

    void handleMessage(final MessageEvent messageEvent);
    void handleData(final DataEvent dataEvent);

}
