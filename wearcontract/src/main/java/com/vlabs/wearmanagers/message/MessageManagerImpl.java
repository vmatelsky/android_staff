package com.vlabs.wearmanagers.message;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.MessageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public class MessageManagerImpl implements MessageManager {

    private final HashMap<String, PublishSubject<MessageEvent>> mOnMessageByPath = new HashMap<>();
    private final HashMap<String, PublishSubject<DataEvent>> mOnDataByPath = new HashMap<>();

    @Override
    public Observable<MessageEvent> onMessageByPath(final String path) {
        return observableByPath(path, mOnMessageByPath);
    }

    @Override
    public Observable<DataEvent> onDataByPath(final String path) {
        return observableByPath(path, mOnDataByPath);
    }

    @Override
    public void handleMessage(final MessageEvent messageEvent) {
        final String path = messageEvent.getPath();
        final PublishSubject<MessageEvent> observable = observableByPath(path, mOnMessageByPath);
        observable.onNext(messageEvent);
//        final DataMap dataMap = DataMap.fromByteArray(messageEvent.getData());
    }

    @Override
    public void handleData(final DataEvent event) {
        final DataItem item = event.getDataItem();
        final String path = item.getUri().getPath();
        final List<PublishSubject<DataEvent>> list = dataReceiversForPath(mOnDataByPath, path);

        for (PublishSubject<DataEvent> observable : list) {
            observable.onNext(event);
        }
    }

    private List<PublishSubject<DataEvent>> dataReceiversForPath(final HashMap<String, PublishSubject<DataEvent>> hashMap, final String path) {
        final ArrayList<PublishSubject<DataEvent>> listenersForPath = new ArrayList<>();
        synchronized (this) {
            for (String listenerPath : hashMap.keySet()) {
                if (path.startsWith(listenerPath) && hashMap.containsKey(listenerPath)) {
                    listenersForPath.add(hashMap.get(listenerPath));
                }
            }
        }
        return listenersForPath;
    }

    private <T> PublishSubject<T> observableByPath(final String path, final HashMap<String, PublishSubject<T>> map) {
        if (!map.containsKey(path)) {
            map.put(path, PublishSubject.<T>create());
        }

        return map.get(path);
    }
}
