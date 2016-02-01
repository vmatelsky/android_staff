package com.vlabs.androiweartest.job;

import android.os.Bundle;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.vlabs.androiweartest.job.base.BaseJob;
import com.vlabs.wearcontract.WearMessage;

import java.util.List;

public class BroadcastMessageJob extends BaseJob {

    private final WearMessage mMessage;

    // as Bundle because Job is serializable while DataMap is not.
    private final Bundle mBundledMap;

    public BroadcastMessageJob(final WearMessage message, final Bundle bundledMap) {
        super();
        mMessage = message;
        mBundledMap = bundledMap;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        final List<Node> connectedNodes = connectedNodes();

        DataMap map = DataMap.fromBundle(mBundledMap);
        for(Node node : connectedNodes) {
            Wearable.MessageApi.sendMessage(googleApiClient(), node.getId(), mMessage.path(), map.toByteArray());
        }
    }

    @Override
    protected void onCancel() {

    }

}
