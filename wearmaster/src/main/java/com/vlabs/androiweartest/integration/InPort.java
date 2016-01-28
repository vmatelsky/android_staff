package com.vlabs.androiweartest.integration;

import com.vlabs.Converter;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.model.Feedback;
import com.vlabs.wearcontract.model.LoadedImage;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class InPort<OuterType, WearType> implements Action1<OuterType> {

    public static class ForYouPin<OuterType> extends InPort<OuterType, List<WearStation>> {

        public ForYouPin(final Observable<OuterType> onChanged, final Converter<OuterType, List<WearStation>> converter) {
            super(onChanged, converter);
        }
    }

    public static class MyStationsPin<OuterType> extends InPort<OuterType, List<WearStation>> {

        public MyStationsPin(final Observable<OuterType> onChanged, final Converter<OuterType, List<WearStation>> converter) {
            super(onChanged, converter);
        }
    }

    public static class RecentlyPlayedPin<OuterType> extends InPort<OuterType, WearStation> {

        public RecentlyPlayedPin(final Observable<OuterType> onChanged, final Converter<OuterType, WearStation> converter) {
            super(onChanged, converter);
        }
    }

    public static class ImageLoadedPin<OuterType> extends InPort<OuterType, LoadedImage> {

        public ImageLoadedPin(final Observable<OuterType> onChanged, final Converter<OuterType, LoadedImage> converter) {
            super(onChanged, converter);
        }
    }

    public static class FeedbackPin extends InPort<Feedback, Feedback> {

        public FeedbackPin(final Observable<Feedback> onChanged, final Converter<Feedback, Feedback> converter) {
            super(onChanged, converter);
        }
    }

    public static class PlayerStateChangedPin<OuterType> extends InPort<OuterType, WearPlayerState> {

        public PlayerStateChangedPin(final Observable<OuterType> onChanged, final Converter<OuterType, WearPlayerState> converter) {
            super(onChanged, converter);
        }
    }

    private final Observable<OuterType> mOnOuterChanged;
    private final PublishSubject<WearType> mOnInnerChanged = PublishSubject.create();
    private final Converter<OuterType, WearType> mConverter;

    public InPort(Observable<OuterType> onChanged, Converter<OuterType, WearType> converter) {
        mOnOuterChanged = onChanged;
        mConverter = converter;
        mOnOuterChanged.subscribe(this);
    }

    public Observable<WearType> onInnerChanged() {
        return mOnInnerChanged;
    }

    @Override
    public void call(final OuterType outerType) {
        mOnInnerChanged.onNext(mConverter.convert(outerType));
    }

}
