package com.vlabs.androiweartest.activity.pick;

import android.app.Activity;
import android.support.wearable.view.WearableListView;

public interface ListItemEntity {
    void bindViewHolder(WearableListView.ViewHolder viewHolder);
    void onClick(Activity activity);
    ListItemView getListItemView();
}
