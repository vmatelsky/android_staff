package com.vlabs.androiweartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        findViewById(R.id.toggle_play).setOnClickListener(v -> togglePlay());

        findViewById(R.id.thumb_up).setOnClickListener(v -> thumbUp());

        findViewById(R.id.thumb_down).setOnClickListener(v -> thumbDown());
    }

    private void thumbDown() {

    }

    private void thumbUp() {

    }

    private void togglePlay() {

    }

}
