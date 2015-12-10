package com.vlabs.uigames;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;


public class SplashActivity extends Activity {

    private final int MAX = 10;
    private AsyncTask<Void, Integer, Void> progressBarUpdater;

    private AsyncTask<Void, Integer, Void> createProgressUpdateTask() {
        return new AsyncTask<Void, Integer, Void>() {

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progressBar.setProgress(values[0]);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i <= MAX; ++i) {
                    try {
                        Thread.sleep(500, 0);
                        publishProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                launchMainActivity();
            }
        };
    }

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setMax(MAX);

        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateLogo();
                progressBarUpdater.cancel(true);
                launchMainActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBarUpdater = createProgressUpdateTask().execute();
//        launchMainActivity();

        animateLogo();
    }

    private void animateLogo() {
        View view = findViewById(R.id.logo_image);
//        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.logo_animator);
//
//        animator.setTarget(view);
//        animator.start();

        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.logo_animation));

//        ScaleInAnimation scaleInAnimation = new ScaleInAnimation();
//        scaleInAnimation.setInterpolator(new AccelerateDecelerateInterpolator(this , R.animator.logo_animator));
//        scaleInAnimation.setDuration(500);
//        scaleInAnimation.animate();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
