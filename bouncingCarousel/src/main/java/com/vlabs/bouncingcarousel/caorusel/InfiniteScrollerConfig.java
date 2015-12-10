package com.vlabs.bouncingcarousel.caorusel;

public final class InfiniteScrollerConfig {
    public InfiniteScrollerConfig(final int leftOffset) {
        mLeftOffset = leftOffset;
    }

    public int leftOffset() {
        return mLeftOffset;
    }

    private final int mLeftOffset;

}
