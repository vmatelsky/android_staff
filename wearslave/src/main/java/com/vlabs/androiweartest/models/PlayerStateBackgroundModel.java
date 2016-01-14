package com.vlabs.androiweartest.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.images.ImageLoader;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearmanagers.Receiver;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class PlayerStateBackgroundModel implements Action1<WearPlayerState> {

    private final Context mContext;
    private final PlayerStateModel mSlaveModel;
    private final ImageLoader mImageLoader;

    private PublishSubject<Drawable> mBackgroundChangedListeners = PublishSubject.create();

    private final Receiver<Bitmap> mBackgroundReceiver = new Receiver<Bitmap>() {
        @Override
        public void receive(final Bitmap bitmap) {
            invokeOnBackgroundChanged(new BitmapDrawable(bitmap));
        }
    };

    public PlayerStateBackgroundModel(
            final Context context,
            final PlayerStateModel model,
            final ImageLoader imageLoader) {
        mContext = context;
        mSlaveModel = model;
        mImageLoader = imageLoader;
        mSlaveModel.onPlayerStateChanged().subscribe(this);
    }

    public Observable<Drawable> onBackgroundChangedListener() {
        return mBackgroundChangedListeners;
    }

    private void invokeOnBackgroundChanged(Drawable drawable) {
        mBackgroundChangedListeners.onNext(drawable);
    }

    private void loadBackgroundImagePath(final String imagePath) {
        mImageLoader.imageByPath(imagePath, mBackgroundReceiver);
    }

    private void loadBackgroundFromRecentStations() {
        // TODO: get background from recently played station
        setDefaultBackgroundColor();
    }

    private void setDefaultBackgroundColor() {
        final int color = ContextCompat.getColor(mContext, R.color.notification_background);
        invokeOnBackgroundChanged(new ColorDrawable(color));
    }

    @Override
    public void call(final WearPlayerState state) {
        if (state.isPlaying()) {
            loadBackgroundImagePath(state.getImagePath());
        } else {
            loadBackgroundFromRecentStations();
        }
    }
}
