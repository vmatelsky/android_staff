package com.vlabs.androiweartest.wear.handlers.message;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.vlabs.androiweartest.connection.ConnectionManager;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.dummy.DummyWearStation;
import com.vlabs.wearcontract.messages.SearchMessage;

import java.util.ArrayList;

public class SearchMessageHandler implements MessageHandler {

    private final ConnectionManager mConnectionManager;

    public SearchMessageHandler(final ConnectionManager connectionManager) {
        mConnectionManager = connectionManager;
    }

    @Override
    public void handle(final MessageEvent messageEvent) {
        final DataMap map = DataMap.fromByteArray(messageEvent.getData());
        SearchMessage message = new SearchMessage(map);
        final String searchTerm = message.term();
        // TODO: perform real search
        deliverBestResult();
    }

    private void deliverBestResult() {
        final PutDataMapRequest putRequest = PutDataMapRequest.create(WearDataEvent.PATH_STATIONS_SEARCH);
        ArrayList<DataMap> mapArray = new ArrayList<>();
        mapArray.add(DummyWearStation.Dummy1.toMap());
        putRequest.getDataMap().putDataMapArrayList(WearDataEvent.KEY_STATIONS, mapArray);
        putRequest.setUrgent();
        mConnectionManager.deleteData(putRequest);
        mConnectionManager.putData(putRequest.asPutDataRequest());
    }
}
