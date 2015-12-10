package com.vlabs.bouncingcarousel.caorusel;

import java.util.List;

public class InfiniteIteratorImpl<ItemType> implements InfiniteIterator<ItemType> {

    private final List<ItemType> mData;
    private final int mIndex;

    public InfiniteIteratorImpl(List<ItemType> data) {
        this(data, 0);
    }

    public InfiniteIteratorImpl(List<ItemType> data, int startIndex) {
        mData = data;
        mIndex = adjustedIndex(startIndex);
    }

    private int adjustedIndex(final int index) {
        final int dataSize = mData.size();

        if (index >= dataSize) {
            return 0;
        } else if (index < 0) {
            return dataSize - 1;
        }

        return index;
    }


    @Override
    public InfiniteIterator<ItemType> next() {
        return new InfiniteIteratorImpl<>(mData, mIndex + 1);
    }

    @Override
    public InfiniteIterator<ItemType> previous() {
        return new InfiniteIteratorImpl<>(mData, mIndex - 1);
    }

    @Override
    public ItemType item() {
        return mData.get(mIndex);
    }
}
