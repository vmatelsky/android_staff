package com.vlabs.wearmanagers.message;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.wearmanagers.Receiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageManagerImpl implements MessageManager {

    private final HashMap<String, ArrayList<Receiver<Message>>> mMessageSubscribers = new HashMap<>();
    private final HashMap<String, ArrayList<Receiver<Message>>> mDataSubscribers = new HashMap<>();

    @Override
    public void subscribeOnMessage(final String path, final Receiver<Message> receiver) {
        addObserver(mMessageSubscribers, path, receiver);
    }

    @Override
    public void unsubscribeOnMessage(final String path, final Receiver<Message> receiver) {
        removeObserver(mMessageSubscribers, path, receiver);
    }

    @Override
    public void subscribeOnData(final String path, final Receiver<Message> receiver) {
        addObserver(mDataSubscribers, path, receiver);
    }

    @Override
    public void unsubscribeOnData(final String path, final Receiver<Message> receiver) {
        removeObserver(mDataSubscribers, path, receiver);
    }

    @Override
    public void handleMessage(final MessageEvent messageEvent) {
        final String path = messageEvent.getPath();
        final List<Receiver<Message>> list = messageReceiversForPath(mMessageSubscribers, path);
        final DataMap dataMap = DataMap.fromByteArray(messageEvent.getData());
        invokeListeners(list, path, dataMap);
    }

    @Override
    public void handleData(final DataEvent event) {
        if (event.getType() == DataEvent.TYPE_CHANGED) {
            final DataItem item = event.getDataItem();
            final String path = item.getUri().getPath();
            final List<Receiver<Message>> list = dataReceiversForPath(mDataSubscribers, path);
            DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
            invokeListeners(list, path, dataMap);
        } else if (event.getType() == DataEvent.TYPE_DELETED) {
            // TODO: add impl for DataItem deleted
        }
    }

    private void addObserver(final HashMap<String, ArrayList<Receiver<Message>>> hashMap, final String path, final Receiver<Message> receiver) {
        ArrayList<Receiver<Message>> subscribers = hashMap.get(path);
        if (subscribers == null) {
            subscribers = new ArrayList<>();
            hashMap.put(path, subscribers);
        }
        if (!subscribers.contains(receiver)) {
            subscribers.add(receiver);
        }
    }

    private void removeObserver(final HashMap<String, ArrayList<Receiver<Message>>> hashMap, final String path, final Receiver<Message> receiver) {
        ArrayList<Receiver<Message>> subscribers = hashMap.get(path);
        if (subscribers != null) {
            subscribers.remove(receiver);
        }
    }

    private ArrayList<Receiver<Message>> messageReceiversForPath(final HashMap<String, ArrayList<Receiver<Message>>> hashMap, final String path) {
        ArrayList<Receiver<Message>> subscribers = hashMap.get(path);
        if (subscribers == null) {
            return new ArrayList<>();
        }

        return subscribers;
    }

    private ArrayList<Receiver<Message>> dataReceiversForPath(final HashMap<String, ArrayList<Receiver<Message>>> hashMap, final String path) {
        final ArrayList<Receiver<Message>> listenersForPath = new ArrayList<>();
        synchronized (this) {
            for (String listenerPath : hashMap.keySet()) {
                if (path.startsWith(listenerPath) && hashMap.containsKey(listenerPath)) {
                    ArrayList<Receiver<Message>> listeners = hashMap.get(listenerPath);
                    for (Receiver<Message> listener : listeners) {
                        listenersForPath.add(listener);
                    }
                }
            }
        }
        return listenersForPath;
    }

    private void invokeListeners(final List<Receiver<Message>> list, final String path, final DataMap map) {
        for (Receiver<Message> receiver : list) {
            receiver.receive(new Message(path, map));
        }
    }
}
