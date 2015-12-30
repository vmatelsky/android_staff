package com.vlabs.androiweartest.activity.pick;

import android.support.wearable.view.WearableListView;
import android.view.View;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.WearApplication;

public enum ListItemView {
    STATION(new ViewHolderCreator() {
        @Override
        public WearableListView.ViewHolder create() {
            return new WearableListView.ViewHolder(View.inflate(WearApplication.instance(), R.layout.list_station_item, null));
        }
    }),
    MESSAGE(new ViewHolderCreator() {
        @Override
        public WearableListView.ViewHolder create() {
            return new WearableListView.ViewHolder(View.inflate(WearApplication.instance(), R.layout.list_message_item, null));
        }
    });

    private final ViewHolderCreator mViewHolderCreator;

    private ListItemView(ViewHolderCreator viewHolderCreator) {
        mViewHolderCreator = viewHolderCreator;
    }

    public int getViewType() {
        return ordinal();
    }

    public WearableListView.ViewHolder createViewHolder() {
        return mViewHolderCreator.create();
    }

    public static WearableListView.ViewHolder getViewHolderFromType(int type) {
        ListItemView[] views = ListItemView.values();
        if(type < 0 || type >= views.length) {
            return null;
        }
        return views[type].createViewHolder();
    }

    private interface ViewHolderCreator {
        WearableListView.ViewHolder create();
    }
}
