package com.vlabs.androiweartest;

import com.vlabs.androiweartest.integration.InPort;
import com.vlabs.androiweartest.oughter.OuterStation;

import java.util.List;

import rx.subjects.PublishSubject;

public class MasterIntegrationModule {

    public static PublishSubject<List<OuterStation>> mForYouStationsObservable = PublishSubject.create();
    public static PublishSubject<List<OuterStation>> mMyStationsObservable = PublishSubject.create();

    public static PublishSubject<OuterStation> mRecentlyPlayedObservable = PublishSubject.create();

    public InPort.ForYouPin<List<OuterStation>> forYouPin = new InPort.ForYouPin<>(mForYouStationsObservable, new OuterStation.StationsListConverter());
    public InPort.MyStationsPin<List<OuterStation>> myStationsPin = new InPort.MyStationsPin<>(mMyStationsObservable, new OuterStation.StationsListConverter());
    public InPort.RecentlyPlayedPin<OuterStation> recentlyPlayedPin = new InPort.RecentlyPlayedPin<>(mRecentlyPlayedObservable, new OuterStation.StationConverter());

}
