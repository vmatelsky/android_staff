package com.vlabs.androiweartest.activity.search;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.models.StationListModel;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearExtras;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearmanagers.message.MessageManager;

import java.util.List;

import rx.functions.Action1;

public class PlayStationActivity extends WearableActivity implements Action1<List<WearStation>> {

    private StationListModel mStationListModel;

    private WearStation mStation;
    private TextView mStationButton;
    private ImageView mStationBackground;

    private final View.OnClickListener onPlayStationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mStation != null) {
                clearPlayButton();
                WearApplication.instance().playerManager().playStation(mStation, mStationListModel.getWearPlayedFrom(mStation));
                tagPlay();
                finishAffinity();
            }
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_station);
        verifyIntent();

        final String titleText = getIntent().getStringExtra(WearExtras.EXTRA_TITLE);

        final String path = getIntent().getStringExtra(WearExtras.EXTRA_STATION_LIST_PATH);
        final MessageManager messageManager = WearApplication.instance().messageManager();
        mStationListModel = new StationListModel(messageManager, path);

        mStationButton = (TextView) findViewById(R.id.station_name_button);
        mStationBackground = (ImageView) findViewById(R.id.station_background);
        final TextView title = (TextView) findViewById(R.id.title);
        title.setText(titleText);

        mStationButton.setOnClickListener(onPlayStationListener);

        mStationListModel.onStationsChanged().subscribe(this);
        mStationListModel.refresh();
    }

    private void updateStationUi() {
        final String stationButtonText;
        if (mStation == null) {
            stationButtonText = "";
            clearPlayButton();
        } else {
            stationButtonText = mStation.name();
            updateStationButton();
        }

        mStationButton.setText(stationButtonText);
    }

    private void clearPlayButton() {
        if(mStationButton != null) {
            mStationButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    private void updateStationButton() {
        if(mStationButton != null) {
            mStationButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableForStation(), null);
        }
    }

    private Drawable drawableForStation() {
        if (mStation == null || mStationButton == null) {
            return null;
        }
        return ContextCompat.getDrawable(this, R.drawable.ic_blackcard_play);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mStationListModel.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mStationListModel.stopListening();
    }

    private void verifyIntent() {
        if (TextUtils.isEmpty(getIntent().getStringExtra(WearExtras.EXTRA_TITLE))) {
            throw new IllegalStateException("No title passed as extra, please pass String as EXTRA_TITLE");
        }
    }

    void tagPlay() {
        WearApplication.instance().analytics().broadcastRemoteAction(WearAnalyticsConstants.WearPlayerAction.PLAY);
    }

    @Override
    public void call(final List<WearStation> wearStations) {
        if (wearStations.isEmpty()) {
            mStationBackground.setImageBitmap(null);
            mStation = null;
        } else {
            // TODO: set image according to image path
            mStationBackground.setImageResource(R.drawable.background_image_default_25);
            mStation = wearStations.get(0);
        }
        updateStationUi();
    }
}
