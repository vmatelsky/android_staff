package com.clearchannel.iheartradio.controller.view;

import android.app.Activity;
import android.support.wearable.view.WearableListView;

import com.vlabs.androiweartest.activity.pick.ListItemEntity;
import com.vlabs.androiweartest.activity.pick.ListItemView;

public class MessageListViewItemEntity implements ListItemEntity {
    private final String mText;

    public MessageListViewItemEntity(String text) {
        mText = text;
    }

    @Override
    public String toString() {
        return "(MessageListViewItemEntity " + mText + ")";
    }

    @Override
    public void bindViewHolder(WearableListView.ViewHolder viewHolder) {
        ((MessageListViewItem) viewHolder.itemView).setMessage(mText);
    }

    @Override
    public void onClick(Activity activity) {

    }

    @Override
    public ListItemView getListItemView() {
        return ListItemView.MESSAGE;
    }
}
