package com.vlabs.androiweartest.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.managers.ImageLoader;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearmanagers.Receiver;

import java.util.ArrayList;
import java.util.List;

public class PlayerStateBackgroundModel {

    private final Context mContext;
    private final PlayerStateModel mSlaveModel;
    private final ImageLoader mImageLoader;
    private final List<Receiver<Drawable>> mBackgroundChangedListeners = new ArrayList<>();

    private final Receiver<WearPlayerState> mPlayerStateChangeListener = new Receiver<WearPlayerState>() {
        @Override
        public void receive(final WearPlayerState state) {
            if (state.isPlaying()) {
                loadBackgroundImagePath(state.getImagePath());
            } else {
                loadBackgroundFromRecentStations();
            }
        }
    };

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
        mSlaveModel.addPlayerStateChangeListener(mPlayerStateChangeListener);
    }

    public void addOnBackgroundChangedListener(Receiver<Drawable> receiver) {
        mBackgroundChangedListeners.add(receiver);
    }

    public void removeOnBackgroundChangedListener(Receiver<Drawable> receiver) {
        mBackgroundChangedListeners.remove(receiver);
    }

    private void invokeOnBackgroundChanged(Drawable drawable) {
        for (Receiver<Drawable> receiver : mBackgroundChangedListeners) {
            receiver.receive(drawable);
        }
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
}
