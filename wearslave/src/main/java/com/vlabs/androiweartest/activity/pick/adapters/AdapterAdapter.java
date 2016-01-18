package com.vlabs.androiweartest.activity.pick.adapters;

import android.support.wearable.view.WearableListView;
import android.view.ViewGroup;

import com.vlabs.androiweartest.activity.BaseActivity;

public class AdapterAdapter extends WearableListView.Adapter implements ClickableAdapter {
    private final WearableListView.Adapter[] mAdapters;
    private final int[] mItemTypeOffsets;

    public AdapterAdapter(final WearableListView.Adapter... adapters) {
        mAdapters = adapters;
        mItemTypeOffsets = new int[adapters.length];

        if (adapters.length > 0) {
            mItemTypeOffsets[0] = 0;

            // we are not assuming that the same item types from different adapters refer to the same actual item type
            // therefore, we are giving each adapter its own range to account for collisions
            for (int i = 0; i < adapters.length - 1; ++i) {
                mItemTypeOffsets[i + 1] = getNextOffset(mItemTypeOffsets[1], mAdapters[1]);
            }
        }
    }

    private static int getNextOffset(final int prevOffset, final WearableListView.Adapter prevAdapter) {
        return prevOffset + 1 + maxItemType(prevAdapter);
    }

    private static int maxItemType(final WearableListView.Adapter adapter) {
        int maxItemType = 0;

        for (int j = 0; j < adapter.getItemCount(); ++j) {
            int itemType = adapter.getItemViewType(j);

            if (itemType >= 0) {
                maxItemType = Math.max(maxItemType, itemType);
            } else {
                throw new RuntimeException("negative item types are not allowed for AdapterAdapter: " + itemType);
            }
        }

        return maxItemType;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int itemType) {
        final AdapterIndex adapterIndex = adjustItemType(itemType);

        return getAdapter(adapterIndex).onCreateViewHolder(viewGroup, adapterIndex.adjustedValue);
    }

    @Override
    public void onBindViewHolder(final WearableListView.ViewHolder viewHolder, final int index) {
        final AdapterIndex adapterIndex = adjustIndex(index);

        getAdapter(adapterIndex).onBindViewHolder(viewHolder, adapterIndex.adjustedValue);
    }

    @Override
    public int getItemCount() {
        int count = 0;

        for (final WearableListView.Adapter adapter : mAdapters) {
            count += adapter.getItemCount();
        }

        return count;
    }

    @Override
    public int getItemViewType(final int index) {
        final AdapterIndex adapterIndex = adjustIndex(index);

        return getAdapter(adapterIndex).getItemViewType(adapterIndex.adjustedValue);
    }

    @Override
    public void handleClick(final BaseActivity activity, final int index) {
        final AdapterIndex adapterIndex = adjustIndex(index);
        final WearableListView.Adapter adapter = getAdapter(adapterIndex);

        if (adapter instanceof ClickableAdapter) {
            ((ClickableAdapter) adapter).handleClick(activity, adapterIndex.adjustedValue);
        }
    }

    private WearableListView.Adapter getAdapter(final AdapterIndex adapterIndex) {
        return mAdapters[adapterIndex.index];
    }

    private AdapterIndex adjustIndex(final int index) {
        int adjustedIndex = index;

        for (int i = 0; i < mAdapters.length; ++i) {
            final int count = mAdapters[i].getItemCount();

            if (count > adjustedIndex) {
                return new AdapterIndex(i, adjustedIndex);
            } else {
                adjustedIndex -= count;
            }
        }

        throw new RuntimeException();
    }

    private AdapterIndex adjustItemType(final int itemType) {
        final int size = mItemTypeOffsets.length;

        for (int i = 0; i < size; ++i) {
            if (i == size - 1 || itemType < mItemTypeOffsets[i + 1]) {
                return new AdapterIndex(i, itemType - mItemTypeOffsets[i]);
            }
        }

        throw new RuntimeException();
    }

    private class AdapterIndex {
        private final int index;
        private final int adjustedValue;

        private AdapterIndex(final int index, final int adjustedValue) {
            this.index = index;
            this.adjustedValue = adjustedValue;
        }
    }
}
