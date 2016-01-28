package com.vlabs.androiweartest.activity.pick.adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;

import com.clearchannel.iheartradio.controller.view.MessageListViewItemEntity;
import com.vlabs.androiweartest.R;
import com.vlabs.wearcontract.helpers.WearAnalyticsConstants;
import com.vlabs.androiweartest.activity.pick.ListItemAdapter;
import com.vlabs.androiweartest.activity.pick.ListItemEntity;
import com.vlabs.androiweartest.activity.pick.adapters.items.StationListItemEntity;
import com.vlabs.wearcontract.WearConstants;
import com.vlabs.wearcontract.WearStation;

import java.util.ArrayList;
import java.util.List;

public class AdapterFactory {
    private final Context mContext;

    public AdapterFactory(final Context context) {
        mContext = context;
    }

    public WearableListView.Adapter createForYouAdapter(final List<WearStation> stations) {
        return new ListItemAdapter(generateForYouListItems(stations));
    }

    public WearableListView.Adapter createMyStationsAdapter(final List<WearStation> stations) {
        return new AdapterAdapter(generateMyStationsAdapters(stations));
    }

    private List<ListItemEntity> generateForYouListItems(final List<WearStation> stations) {
        final List<ListItemEntity> items = new ArrayList<ListItemEntity>();

        for (final WearStation station : stations) {
            items.add(new StationListItemEntity(station, WearAnalyticsConstants.WearPlayedFrom.FOR_YOU));
        }

        items.add(new MessageListViewItemEntity(mContext.getString(R.string.show_more)));
        return items;
    }

    private WearableListView.Adapter[] generateMyStationsAdapters(final List<WearStation> stations) {
        final List<ListItemEntity> favoriteStations = getItemsFilteredByFavorite(stations, true);
        final List<ListItemEntity> recentStations = getItemsFilteredByFavorite(stations, false);
        final List<WearableListView.Adapter> adapters = new ArrayList<WearableListView.Adapter>();

        if (!favoriteStations.isEmpty()) {
            adapters.add(new ListItemAdapter(formatMyStationsListItems(favoriteStations, WearConstants.FAVORITES_LIMIT, R.string.favorite_stations_title)));
        }

        if (!recentStations.isEmpty()) {
            final int recentStationsLimit = WearConstants.MY_STATIONS_LIMIT - Math.min(favoriteStations.size(), WearConstants.FAVORITES_LIMIT);
            adapters.add(new ListItemAdapter(formatMyStationsListItems(recentStations, recentStationsLimit, R.string.recent_stations_title)));
        }

        if (favoriteStations.size() > WearConstants.FAVORITES_LIMIT) {
            final ArrayList<ListItemEntity> items = new ArrayList<ListItemEntity>();
            items.add(new MessageListViewItemEntity(mContext.getString(R.string.not_all_favorites_showing_message)));
            adapters.add(new ListItemAdapter(items));
        }

        return adapters.toArray(new WearableListView.Adapter[adapters.size()]);
    }

    private List<ListItemEntity> formatMyStationsListItems(final List<ListItemEntity> items, final int limit, final int titleResid) {
        final List<ListItemEntity> formattedItems = new ArrayList<ListItemEntity>();
        final int limitedSize = Math.min(items.size(), limit);

        formattedItems.add(new MessageListViewItemEntity(mContext.getString(titleResid)));

        for (int i = 0; i < limitedSize; ++i) {
            formattedItems.add(items.get(i));
        }

        return formattedItems;
    }

    private List<ListItemEntity> getItemsFilteredByFavorite(final List<WearStation> stations, final boolean isFavorite) {
        final List<ListItemEntity> result = new ArrayList<>();
        final WearAnalyticsConstants.WearPlayedFrom playedFrom = isFavorite ? WearAnalyticsConstants.WearPlayedFrom.MY_STATIONS_FAVORITE : WearAnalyticsConstants.WearPlayedFrom.MY_STATIONS_RECENT;

        for (final WearStation station : stations) {
            if (station.isFavorite() == isFavorite) {
                result.add(new StationListItemEntity(station, playedFrom));
            }
        }

        return result;
    }
}
