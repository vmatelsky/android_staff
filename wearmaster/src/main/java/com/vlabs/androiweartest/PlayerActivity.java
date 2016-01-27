package com.vlabs.androiweartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vlabs.androiweartest.wear.managers.WearPlayerManager;

public class PlayerActivity extends AppCompatActivity {

    private WearPlayerManager mPlayerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mPlayerManager = MasterApplication.instance().wearFacade().playerManager();

        findViewById(R.id.toggle_play).setOnClickListener(this::togglePlay);

        findViewById(R.id.thumb_up).setOnClickListener(this::thumbUp);

        findViewById(R.id.thumb_down).setOnClickListener(this::thumbDown);
    }

    private void thumbDown(View view) {
        mPlayerManager.thumbDown();
    }

    private void thumbUp(View view) {
        mPlayerManager.thumbUp();
    }

    private void togglePlay(View view) {
        mPlayerManager.togglePlay();
    }

}
