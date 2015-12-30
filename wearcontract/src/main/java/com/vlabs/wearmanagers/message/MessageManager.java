package com.vlabs.wearmanagers.message;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.wearmanagers.Receiver;

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

    void subscribeOnMessage(final String path, final Receiver<Message> receiver);
    void unsubscribeOnMessage(final String path, final Receiver<Message> receiver);

    void subscribeOnData(final String path, final Receiver<Message> receiver);
    void unsubscribeOnData(final String path, final Receiver<Message> receiver);

    void handleMessage(final MessageEvent messageEvent);
    void handleData(final DataEvent dataEvent);

}
