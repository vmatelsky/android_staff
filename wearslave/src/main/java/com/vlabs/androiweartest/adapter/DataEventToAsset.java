package com.vlabs.androiweartest.adapter;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;

import rx.functions.Action1;

public abstract class DataEventToAsset implements Action1<DataEvent> {

    @Override
    public void call(final DataEvent event) {
        if (event.getType() == DataEvent.TYPE_CHANGED) {
            final DataItem item = event.getDataItem();
            final String path = item.getUri().getPath();

            final DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
            onDataReceived(path, dataMap);
        } else if (event.getType() == DataEvent.TYPE_DELETED) {
            // TODO: add impl for DataItem deleted
        }
    }

    public abstract void onDataReceived(final String path, final DataMap dataMap);
}
