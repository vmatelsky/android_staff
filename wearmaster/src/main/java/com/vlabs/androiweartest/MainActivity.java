package com.vlabs.androiweartest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.vlabs.androiweartest.oughter.model.ForYouModelWearAdapter;
import com.vlabs.androiweartest.oughter.model.MyStationsWearAdapter;
import com.vlabs.androiweartest.wear.managers.RecentlyPlayedManager;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.dummy.DummyWearStation;
import com.vlabs.wearcontract.messages.FeedbackMessage;

import java.io.ByteArrayOutputStream;

import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "main-bla-bla";

    private ForYouModelWearAdapter mForYouAdapter = new ForYouModelWearAdapter();
    private MyStationsWearAdapter mMyStationsAdapter = new MyStationsWearAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.shuffle_my_stations).setOnClickListener(v -> shuffleMyStations());

        findViewById(R.id.shuffle_for_you).setOnClickListener(v -> shuffleForYou());

        findViewById(R.id.send_image).setOnClickListener(v -> sendImage());

        findViewById(R.id.send_feedback).setOnClickListener(v -> sendFeedback());

        findViewById(R.id.send_recently_played).setOnClickListener(v -> sendRecentlyPlayed());

        findViewById(R.id.open_player).setOnClickListener(v -> openPlayer());
    }

    private void sendRecentlyPlayed() {
        PublishSubject<WearStation> observable = PublishSubject.create();
        observable.onNext(DummyWearStation.Dummy2);
    }

    private void openPlayer() {
        final Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }

    private void sendFeedback() {
        MasterApplication.instance().wearFacade().connectionManager().broadcastMessage(WearMessage.FEEDBACK, new FeedbackMessage("Test feedback").asDataMap());
    }

    private void sendImage() {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image_to_send);
        final Asset asset = createAssetFromBitmap(bitmap);

        PutDataMapRequest dataMap = PutDataMapRequest.create(WearDataEvent.PATH_IMAGE_LOADED);
        dataMap.getDataMap().putAsset(WearDataEvent.KEY_IMAGE_ASSET, asset);
        dataMap.getDataMap().putString(WearDataEvent.KEY_IMAGE_PATH, "/image1234");
        PutDataRequest request = dataMap.asPutDataRequest();
        MasterApplication.instance().wearFacade().connectionManager().putData(request);
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
        mMyStationsAdapter.refresh();
    }

}
