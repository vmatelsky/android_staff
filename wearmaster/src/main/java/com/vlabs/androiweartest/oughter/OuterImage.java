package com.vlabs.androiweartest.oughter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.wearable.Asset;
import com.vlabs.Converter;
import com.vlabs.androiweartest.MasterApplication;
import com.vlabs.androiweartest.R;
import com.vlabs.wearcontract.dummy.DummyWearStation;
import com.vlabs.wearcontract.messages.LoadImageMessage;
import com.vlabs.wearcontract.model.LoadedImage;

import java.io.ByteArrayOutputStream;

public class OuterImage {

    public static class ImageConverter implements Converter<OuterImage, LoadedImage> {

        @Override
        public LoadedImage convert(final OuterImage outerImage) {
            final LoadImageMessage message = outerImage.mLoadImageMessage;
            final Bitmap bitmap = BitmapFactory.decodeResource(MasterApplication.instance().getResources(), imageByPath(message.imagePath()));
            final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, message.width(), message.height(), false);
            final Asset asset = createAssetFromBitmap(scaledBitmap);

            return new LoadedImage(asset, message.imagePath());
        }

        private static int imageByPath(final String path) {
            switch (path) {
                case DummyWearStation.DUMMY_IMAGE_PATH_1:
                    return R.drawable.dummy_station_1;
                case DummyWearStation.DUMMY_IMAGE_PATH_2:
                    return R.drawable.dummy_station_2;
                case DummyWearStation.DUMMY_IMAGE_PATH_3:
                    return R.drawable.dummy_station_3;
                case DummyWearStation.DUMMY_IMAGE_PATH_4:
                    return R.drawable.dummy_station_4;
                case DummyWearStation.DUMMY_IMAGE_PATH_5:
                    return R.drawable.dummy_station_5;
                default:
                    return R.drawable.test_image_to_send;
            }
        }

        private static Asset createAssetFromBitmap(Bitmap bitmap) {
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            return Asset.createFromBytes(byteStream.toByteArray());
        }

    }

    public final LoadImageMessage mLoadImageMessage;

    public OuterImage(final LoadImageMessage loadImageMessage) {
        mLoadImageMessage = loadImageMessage;
    }


}
