package com.vlabs.androiweartest;

import com.vlabs.Converter;
import com.vlabs.androiweartest.integration.InPort;
import com.vlabs.androiweartest.oughter.OuterImage;
import com.vlabs.androiweartest.oughter.OuterStation;
import com.vlabs.wearcontract.model.Feedback;

import java.util.List;

import rx.subjects.PublishSubject;

public class MasterIntegrationModule {

    public static PublishSubject<List<OuterStation>> mForYouStationsObservable = PublishSubject.create();
    public static PublishSubject<List<OuterStation>> mMyStationsObservable = PublishSubject.create();

    public static PublishSubject<OuterImage> mLoadedImageObservable = PublishSubject.create();
    public static PublishSubject<OuterStation> mRecentlyPlayedObservable = PublishSubject.create();
    public static PublishSubject<Feedback> mOnFeedbackObservable = PublishSubject.create();

    public InPort.ForYouPin<List<OuterStation>> forYouPin = new InPort.ForYouPin<>(mForYouStationsObservable, new OuterStation.StationsListConverter());
    public InPort.MyStationsPin<List<OuterStation>> myStationsPin = new InPort.MyStationsPin<>(mMyStationsObservable, new OuterStation.StationsListConverter());
    public InPort.RecentlyPlayedPin<OuterStation> recentlyPlayedPin = new InPort.RecentlyPlayedPin<>(mRecentlyPlayedObservable, new OuterStation.StationConverter());
    public InPort.ImageLoadedPin<OuterImage> imageLoadedPin = new InPort.ImageLoadedPin<>(mLoadedImageObservable, new OuterImage.ImageConverter());
    public InPort.FeedbackPin feedbackPin = new InPort.FeedbackPin(mOnFeedbackObservable, new Converter.SelfConverter<>());

}
