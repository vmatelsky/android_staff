package com.vlabs.androiweartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vlabs.androiweartest.model.ForYouModelWearAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "main-bla-bla";

    private ForYouModelWearAdapter mForYouAdapter = new ForYouModelWearAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.shuffle_my_stations).setOnClickListener(v -> shuffleMyStations());

        findViewById(R.id.shuffle_for_you).setOnClickListener(v -> shuffleForYou());
    }

    private void shuffleForYou() {
        mForYouAdapter.refresh();
    }

    private void shuffleMyStations() {

    }

}
