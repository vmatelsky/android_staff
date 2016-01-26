package com.vlabs.androiweartest.di.component;

import com.vlabs.androiweartest.activity.launch.WearMainActivity;
import com.vlabs.androiweartest.activity.notification.NotificationActivity;
import com.vlabs.androiweartest.activity.pick.PickStationActivity;
import com.vlabs.androiweartest.activity.pick.adapters.items.StationListItemEntity;
import com.vlabs.androiweartest.activity.search.PlayStationActivity;
import com.vlabs.androiweartest.activity.search.SearchActivity;
import com.vlabs.androiweartest.activity.showMore.ShowMoreActivity;
import com.vlabs.androiweartest.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent {

    void inject(PickStationActivity pickStationActivity);

    void inject(PlayStationActivity playStationActivity);

    void inject(SearchActivity searchActivity);

    void inject(StationListItemEntity stationListItemEntity);

    void inject(NotificationActivity notificationActivity);

    void inject(ShowMoreActivity showMoreActivity);

    void inject(WearMainActivity wearMainActivity);
}
