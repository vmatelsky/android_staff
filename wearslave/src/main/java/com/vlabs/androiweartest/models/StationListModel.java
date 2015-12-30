package com.vlabs.androiweartest.models;

import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearmanagers.Receiver;
import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.message.MessageManager;

import java.util.ArrayList;
import java.util.List;

public class StationListModel {

    private final String mPath;
    private final MessageManager mMessageManager;
    private final List<Receiver<List<WearStation>>> mStationsChangedReceivers = new ArrayList<>();
    private Receiver<MessageManager.Message> mOnStationListChanged = new Receiver<MessageManager.Message>() {
        @Override
        public void receive(final MessageManager.Message message) {
            onStationsChanged(processDataMap(message.dataMap()));
        }
    };

    private List<WearStation> processDataMap(final DataMap dataMap) {
        if (dataMap == null) return new ArrayList<>();

        final ArrayList<DataMap> dataMapArrayList = dataMap.getDataMapArrayList(Data.KEY_STATIONS);

        if (dataMapArrayList == null) return new ArrayList<>();

        List<WearStation> stations = new ArrayList<>(dataMapArrayList.size());

        for (DataMap stationMap : dataMapArrayList) {
            stations.add(WearStation.fromDataMap(stationMap));
        }

        return stations;
    }


    public StationListModel(final MessageManager messageManager, final String path) {
        mPath = path;
        mMessageManager = messageManager;
    }

    public void startListening() {
        mMessageManager.subscribeOnData(mPath, mOnStationListChanged);
    }

    public void stopListening() {
        mMessageManager.unsubscribeOnData(mPath, mOnStationListChanged);
    }

    public void addOnStationsChangedListener(Receiver<List<WearStation>> receiver) {
        mStationsChangedReceivers.add(receiver);
    }

    public void removeOnStationsChangedListener(Receiver<List<WearStation>> receiver) {
        mStationsChangedReceivers.remove(receiver);
    }

    private void onStationsChanged(List<WearStation> stations) {
        for (Receiver<List<WearStation>> receiver : mStationsChangedReceivers) {
            receiver.receive(stations);
        }
    }

    public void getData(final Receiver<List<WearStation>> receiver) {
        WearApplication.instance().connectionManager().getDataItems(mPath, new ConnectionManager.DataListener() {
            @Override
            public void onData(final String path, final DataMap map) {
                receiver.receive(processDataMap(map));
            }
        });
    }
}
