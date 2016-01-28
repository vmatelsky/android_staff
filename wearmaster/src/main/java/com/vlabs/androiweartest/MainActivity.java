package com.vlabs.androiweartest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vlabs.androiweartest.oughter.OuterImage;
import com.vlabs.androiweartest.oughter.model.ForYouModelWearAdapter;
import com.vlabs.androiweartest.oughter.model.MyStationsWearAdapter;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.dummy.DummyWearStation;
import com.vlabs.wearcontract.messages.LoadImageMessage;
import com.vlabs.wearcontract.model.Feedback;

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
        MasterApplication.instance().integrationModule().feedbackPin.call(new Feedback("Test feedback"));
    }

    private void sendImage() {
        MasterApplication.instance().integrationModule().imageLoadedPin.call(new OuterImage(new LoadImageMessage("/image1234", 100, 100)));
    }

    private void shuffleForYou() {
        mForYouAdapter.refresh();
    }

    private void shuffleMyStations() {
        mMyStationsAdapter.refresh();
    }

}
