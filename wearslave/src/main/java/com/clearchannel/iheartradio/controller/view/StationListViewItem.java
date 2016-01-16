package com.clearchannel.iheartradio.controller.view;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vlabs.androiweartest.R;
import com.vlabs.wearcontract.WearStation;

public class StationListViewItem extends LinearLayout implements WearableListView.OnCenterProximityListener {

    private final float mFadedTextAlpha;
    private final float mFadedImageAlpha;
    private ImageByDataPathView mImage;
    private TextView mName;

    public StationListViewItem(Context context) {
        this(context, null);
    }

    public StationListViewItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StationListViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mFadedTextAlpha = context.getResources().getInteger(R.integer.list_item_text_alpha_faded) / 100f;
        mFadedImageAlpha = context.getResources().getInteger(R.integer.list_item_image_alpha_faded) / 100f;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mImage = (ImageByDataPathView) findViewById(R.id.image);
        mName = (TextView) findViewById(R.id.name);
    }

    public void setStation(WearStation station) {
        mImage.setImageResource(R.drawable.background_image_default_25);
        mImage.setImagePath(station.getImagePath());
        mName.setText(station.name());
    }

    @Override
    public String toString() {
        return "StationListViewItem{" +
                "mFadedTextAlpha=" + mFadedTextAlpha +
                ", mName=" + mName.getText() +
                ", mImage=" + mImage +
                '}';
    }

    @Override
    public void onCenterPosition(final boolean b) {
        //Animation example to be ran when the view becomes the centered one
        mImage.animate().scaleX(1f).scaleY(1f).alpha(1);
        mName.animate().scaleX(1f).scaleY(1f).alpha(1);
    }

    @Override
    public void onNonCenterPosition(final boolean b) {
        //Animation example to be ran when the view is not the centered one anymore
        mImage.animate().scaleX(0.7f).scaleY(0.7f).alpha(mFadedImageAlpha);
        mName.animate().scaleX(0.7f).scaleY(0.7f).alpha(mFadedTextAlpha);
    }
}
