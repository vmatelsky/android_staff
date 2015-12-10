package com.vlabs.bouncingcarousel;

import android.graphics.drawable.Drawable;

public class CarouselItem {
    private final String mText;

    public CarouselItem(String text) {
        mText = text;
    }

    public String text() {
        return mText;
    }
}
