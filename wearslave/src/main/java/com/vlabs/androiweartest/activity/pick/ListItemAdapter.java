package com.vlabs.androiweartest.activity.pick;

import android.support.wearable.view.WearableListView;
import android.view.ViewGroup;

import com.vlabs.androiweartest.activity.BaseActivity;
import com.vlabs.androiweartest.activity.pick.adapters.ClickableAdapter;

import java.util.List;

public class ListItemAdapter extends WearableListView.Adapter implements ClickableAdapter {

    private final List<ListItemEntity> mItems;

    public ListItemAdapter(final List<ListItemEntity> items) {
        mItems = items;
    }

    @Override
    public String toString() {
        return "(ListItemAdapter " + mItems + ")";
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int itemType) {
        return ListItemView.getViewHolderFromType(itemType);
    }

    @Override
    public void onBindViewHolder(final WearableListView.ViewHolder viewHolder, final int index) {
        mItems.get(index).bindViewHolder(viewHolder);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(final int index) {
        return mItems.get(index).getListItemView().getViewType();
    }

    @Override
    public void handleClick(final BaseActivity activity, final int index) {
        mItems.get(index).onClick(activity);
    }
}
