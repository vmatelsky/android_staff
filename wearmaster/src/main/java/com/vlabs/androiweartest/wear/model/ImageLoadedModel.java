package com.vlabs.androiweartest.wear.model;

import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.vlabs.androiweartest.integration.InPort;
import com.vlabs.androiweartest.oughter.OuterImage;
import com.vlabs.androiweartest.wear.connection.ConnectionManager;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.model.LoadedImage;

import rx.functions.Action1;

public class ImageLoadedModel<OuterImage> implements Action1<LoadedImage> {

    private final ConnectionManager mConnectionManager;

    public ImageLoadedModel(final InPort.ImageLoadedPin<OuterImage> imageLoadedPin, final ConnectionManager connectionManager) {
        mConnectionManager = connectionManager;
        imageLoadedPin.onInnerChanged().subscribe(this);
    }

    @Override
    public void call(final LoadedImage loadedImage) {
        final PutDataMapRequest dataMap = PutDataMapRequest.create(WearDataEvent.PATH_IMAGE_LOADED);
        dataMap.getDataMap().putAsset(WearDataEvent.KEY_IMAGE_ASSET, loadedImage.asset());
        dataMap.getDataMap().putString(WearDataEvent.KEY_IMAGE_PATH, loadedImage.imagePath());
        final PutDataRequest request = dataMap.asPutDataRequest();
        mConnectionManager.putData(request);
    }
}
