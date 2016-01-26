package com.clearchannel.iheartradio.controller.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.events.data.OnImageLoaded;
import com.vlabs.androiweartest.images.ImageManager;

import java.util.Objects;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class ImageByDataPathView extends ImageView {

    @Inject
    ImageManager mImageManager;

    @Inject
    EventBus mEventBus;

    private String mPath;

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

        mPath = newPath;
        getImage();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mEventBus.register(this);
        getImage();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mEventBus.unregister(this);
    }

    private void getImage() {
        mImageManager.requestImage(mPath, this::processBitmap);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final OnImageLoaded event) {
        if (isAttachedToWindow()) {
            if (event.path().equals(mPath)) {
                processBitmap(event.image());
            }
        }
    }

    private void processBitmap(final Bitmap bitmap) {
        setImageBitmap(bitmap);
    }
}