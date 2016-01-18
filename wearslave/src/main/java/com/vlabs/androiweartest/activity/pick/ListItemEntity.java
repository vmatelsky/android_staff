package com.vlabs.androiweartest.activity.pick;

import android.support.wearable.view.WearableListView;

import com.vlabs.androiweartest.activity.BaseActivity;

public interface ListItemEntity {
    void bindViewHolder(WearableListView.ViewHolder viewHolder);
    void onClick(BaseActivity activity);
    ListItemView getListItemView();
}
