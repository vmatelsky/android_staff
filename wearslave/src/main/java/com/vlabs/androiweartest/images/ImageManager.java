package com.vlabs.androiweartest.images;

import android.graphics.Bitmap;

import com.vlabs.androiweartest.events.data.OnImageLoaded;
import com.vlabs.Receiver;

import de.greenrobot.event.EventBus;

public class ImageManager {

    private final ImageCache mImageCache = new ImageCache();
    private final ImageLoader mImageLoader;

    public ImageManager(
            final ImageLoader imageLoader,
            final EventBus eventBus) {
        mImageLoader = imageLoader;
        eventBus.register(this);
    }

    public void requestImage(final String imagePath, Receiver<Bitmap> ifCached) {
        if (imagePath == null) return;

        final Bitmap fromCache = mImageCache.getBitmapFromMemCache(imagePath);

        if (fromCache != null) {
            ifCached.receive(fromCache);
        }

        mImageLoader.imageByPath(imagePath);
    }

    @SuppressWarnings("unused")
    public void onEventBackgroundThread(OnImageLoaded event) {
        mImageCache.addBitmapToMemoryCache(event.path(), event.image());
    }

}
