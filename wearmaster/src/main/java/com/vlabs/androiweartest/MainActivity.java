package com.vlabs.androiweartest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.vlabs.androiweartest.model.ForYouModelWearAdapter;
import com.vlabs.wearcontract.Data;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "main-bla-bla";

    private ForYouModelWearAdapter mForYouAdapter = new ForYouModelWearAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.shuffle_my_stations).setOnClickListener(v -> shuffleMyStations());

        findViewById(R.id.shuffle_for_you).setOnClickListener(v -> shuffleForYou());

        findViewById(R.id.send_image).setOnClickListener(v -> sendImage());
    }

    private void sendImage() {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image_to_send);
        final Asset asset = createAssetFromBitmap(bitmap);
        final PutDataMapRequest dataMap = PutDataMapRequest.create("/image1234");

        dataMap.getDataMap().putAsset(Data.KEY_IMAGE_ASSET, asset);
        dataMap.setUrgent();

        MasterApplication.instance().connectionManager().putData(dataMap);
    }

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }

    private void shuffleForYou() {
        mForYouAdapter.refresh();
    }

    private void shuffleMyStations() {

    }

}
