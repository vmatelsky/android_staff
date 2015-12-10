package com.vlabs.progresswidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressWidget progress = (ProgressWidget) findViewById(R.id.progress_widget);
        final ProgressWidget secondWidget = (ProgressWidget) findViewById(R.id.second_widget);

        View button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Random rand = new Random();
                progress.setProgress(rand.nextInt(100));
                secondWidget.setProgress(rand.nextInt(100));
            }
        });
    }

}
