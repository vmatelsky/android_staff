package com.vlabs.wearmanagers;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.vlabs.wearmanagers.connection.ConnectionManager;

public class WearUtils {

    public static void getDataItems(final GoogleApiClient apiClient, final String path, final ConnectionManager.DataListener onDataReady) {
        new AsyncTask<Void, Void, DataMap>() {
            @Override
            protected DataMap doInBackground(Void... params) {
                Node node = getMainNodeSync(apiClient);
                if (node == null) {
                    return null;
                }

                Uri dataUri = dataUriForNode(node).buildUpon().path(path).build();
                DataApi.DataItemResult dataResult = Wearable.DataApi.getDataItem(apiClient, dataUri).await();
                DataItem dataItem = dataResult.getDataItem();
                if (dataItem == null) {
                    return null;
                }

                return DataMapItem.fromDataItem(dataItem).getDataMap();
            }

            @Override
            protected void onPostExecute(DataMap dataMap) {
                super.onPostExecute(dataMap);
                onDataReady.onData(path, dataMap);
            }
        }.execute();
    }

    private static Uri dataUriForNode(Node node) {
        return new Uri.Builder().scheme(PutDataRequest.WEAR_URI_SCHEME).authority(node.getId()).build();
    }

    private static Node getMainNodeSync(final GoogleApiClient apiClient) {
        NodeApi.GetConnectedNodesResult nodeResult = Wearable.NodeApi.getConnectedNodes(apiClient).await();
        if (nodeResult.getNodes().isEmpty()) {
            return null;
        }
        else {
            return nodeResult.getNodes().get(0);
        }
    }
}
