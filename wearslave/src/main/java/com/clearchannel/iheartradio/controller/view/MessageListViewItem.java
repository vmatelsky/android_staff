package com.clearchannel.iheartradio.controller.view;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vlabs.androiweartest.R;

public class MessageListViewItem extends LinearLayout implements WearableListView.OnCenterProximityListener {

    private TextView mMessage;

    public MessageListViewItem(Context context) {
        this(context, null);
    }

    public MessageListViewItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageListViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMessage = (TextView) findViewById(R.id.message);
    }


    public void setMessage(String message) {
        mMessage.setText(message);
    }

    @Override
    public void onCenterPosition(final boolean b) {

    }

    @Override
    public void onNonCenterPosition(final boolean b) {

    }
}

