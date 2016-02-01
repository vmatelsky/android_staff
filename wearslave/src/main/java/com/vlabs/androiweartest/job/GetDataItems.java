package com.vlabs.androiweartest.job;

import android.net.Uri;

import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.vlabs.androiweartest.events.data.OnDataReceived;
import com.vlabs.androiweartest.job.base.BaseJob;

public class GetDataItems extends BaseJob {

    private final String mPath;

    public GetDataItems(final String path) {
        super();
        mPath = path;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Node node = getMainNodeSync(googleApiClient());

        if (node == null) return;

        final Uri dataUri = dataUriForNode(node).buildUpon().path(mPath).build();

        final DataApi.DataItemResult dataResult = Wearable.DataApi.getDataItem(googleApiClient(), dataUri).await();

        final DataItem dataItem = dataResult.getDataItem();
        if (dataItem == null) return;

        final DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();

        eventBus().post(new OnDataReceived(mPath, dataMap));
    }

    @Override
    protected void onCancel() {

    }
}
