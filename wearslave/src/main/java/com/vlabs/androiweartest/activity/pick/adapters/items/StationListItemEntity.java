package com.vlabs.androiweartest.activity.pick.adapters.items;

import android.support.wearable.view.WearableListView;
import android.widget.Toast;

import com.clearchannel.iheartradio.controller.view.StationListViewItem;
import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.activity.BaseActivity;
import com.vlabs.androiweartest.activity.pick.ListItemEntity;
import com.vlabs.androiweartest.activity.pick.ListItemView;
import com.vlabs.androiweartest.helpers.analytics.WearAnalyticsConstants;
import com.vlabs.androiweartest.models.PlayerManager;
import com.vlabs.wearcontract.WearStation;

import javax.inject.Inject;

public class StationListItemEntity implements ListItemEntity {

    @Inject
    PlayerManager mPlayerManager;

    private final WearStation mWearStation;
    private final WearAnalyticsConstants.WearPlayedFrom mPlayedFrom;

    public StationListItemEntity(WearStation wearStation, WearAnalyticsConstants.WearPlayedFrom playedFrom) {
        mWearStation = wearStation;
        mPlayedFrom = playedFrom;
    }

    @Override
    public String toString() {
        return "(" + mWearStation.name() + " " + mPlayedFrom + ")";
    }

    @Override
    public void bindViewHolder(WearableListView.ViewHolder viewHolder) {
        ((StationListViewItem)viewHolder.itemView).setStation(mWearStation);
    }

    @Override
    public void onClick(BaseActivity activity) {
        Toast.makeText(activity.getApplicationContext(), R.string.wear_loading, Toast.LENGTH_LONG).show();
        activity.getComponent().inject(this);
        mPlayerManager.playStation(mWearStation, mPlayedFrom);
        activity.finishAffinity();
    }

    @Override
    public ListItemView getListItemView() {
        return ListItemView.STATION;
    }
}
