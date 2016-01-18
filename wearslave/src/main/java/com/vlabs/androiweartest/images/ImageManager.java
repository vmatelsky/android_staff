package com.vlabs.androiweartest.images;

import android.graphics.Bitmap;

import com.vlabs.wearmanagers.Receiver;
import com.vlabs.wearmanagers.connection.ConnectionManager;


public class ImageManager implements ConnectionManager.ImageListener {

    private final ImageCache mImageCache = new ImageCache();
    private final ImageLoader mImageLoader;

    public ImageManager(final ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public void requestImage(final String imagePath, final Receiver<Bitmap> receiver) {
        final Bitmap fromCache = mImageCache.getBitmapFromMemCache(imagePath);

        if (fromCache != null) {
            receiver.receive(fromCache);
        }

        mImageLoader.imageByPath(imagePath, bitmap -> {
            onImage(imagePath, bitmap);
            receiver.receive(bitmap);
        });
    }

    @Override
    public void onImage(final String path, final Bitmap bitmap) {
        mImageCache.addBitmapToMemoryCache(path, bitmap);
    }
}
