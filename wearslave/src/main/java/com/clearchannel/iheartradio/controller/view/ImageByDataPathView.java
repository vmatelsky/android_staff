package com.clearchannel.iheartradio.controller.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.message.MessageManager;

import java.util.Objects;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;

public class ImageByDataPathView extends ImageView implements Action1<DataEvent>, ConnectionManager.ImageListener, ConnectionManager.DataListener {

    @Inject
    ConnectionManager mConnectionManager;

    @Inject
    MessageManager mMessageManager;

    private String mPath;
    private Subscription mCurrentSubscription;
    private Subscription mOnConnectedSubscription;

    public ImageByDataPathView(Context context) {
        super(context);
        WearApplication.instance().appComponent().inject(this);
    }

    public ImageByDataPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WearApplication.instance().appComponent().inject(this);
    }

    public ImageByDataPathView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        WearApplication.instance().appComponent().inject(this);
    }

    public void setImagePath(final String newPath) {
        if (Objects.equals(newPath, mPath)) return;

        if (mCurrentSubscription != null) {
            mCurrentSubscription.unsubscribe();
        }

        mPath = newPath;
        getImage();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPath == null) return;
        mCurrentSubscription = subscribeOnceByPath(mCurrentSubscription, mPath);

        if (mConnectionManager.isConnected()) {
            getImage();
        } else {
            mOnConnectedSubscription = mConnectionManager.onConnected().subscribe(aVoid -> {
                getImage();
            });
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mCurrentSubscription = getUnsubscribed(mCurrentSubscription);
        mOnConnectedSubscription = getUnsubscribed(mOnConnectedSubscription);
    }

    public Subscription getUnsubscribed(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        return null;
    }

    private void getImage() {
        if (mPath == null) {
            setImageBitmap(null);
        } else {
            mCurrentSubscription = subscribeOnceByPath(mCurrentSubscription, mPath);
            mConnectionManager.getDataItems(mPath, this);
        }
    }

    private Subscription subscribeOnceByPath(final Subscription currentSubscription, final String path) {
        if (currentSubscription == null || currentSubscription.isUnsubscribed()) {
            return mMessageManager.onDataByPath(path).subscribe(this);
        }
        return currentSubscription;
    }

    @Override
    public void call(final DataEvent dataEvent) {
        if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
            final DataMap dataMap = DataMap.fromByteArray(dataEvent.getDataItem().getData());
            handleDataMap(dataMap);
        } else {
            setImageBitmap(null);
        }
    }

    @Override
    public void onImage(final String path, final Bitmap bitmap) {
        setImageBitmap(bitmap);
    }

    @Override
    public void onData(final String path, final DataMap map) {
        handleDataMap(map);
    }

    private void handleDataMap(final DataMap map) {
        if (map == null) return;

        final Asset asset = map.getAsset(Data.KEY_IMAGE_ASSET);
        mConnectionManager.getAssetAsBitmap(mPath, asset, this);
    }
}