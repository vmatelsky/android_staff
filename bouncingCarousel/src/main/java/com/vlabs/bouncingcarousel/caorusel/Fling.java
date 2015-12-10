package com.vlabs.bouncingcarousel.caorusel;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public abstract class Fling<ItemType> {

    public static int FLING_DURATION = 500; // duration in ms

    final List<ViewPortItem<ItemType>> mItems;
    final int mItemWidth;

    public interface FlingListener<ItemType> {
        void onItemMoved(ViewPortItem<ItemType> item);
    }

    public Fling(List<ViewPortItem<ItemType>> items, int itemWidth) {
        mItems = items;
        mItemWidth = itemWidth;
    }

    public Animator fling(ViewPortItem<ItemType> rootItem, final float distanceToFling, final FlingListener<ItemType> listener) {
        final int rootIndex = mItems.indexOf(rootItem);
        final List<Animator> itemAnimators = new ArrayList<>();

        traverseItemsList(new OnItem<ItemType>() {
            @Override
            public void onItem(final int itemIndex, final ViewPortItem<ItemType> item) {
                final float startOffset = item.leftAnchor();
                final float finalItemOffset = computeFinalItemOffset(item.leftAnchor(), distanceToFling);
                ValueAnimator animatorForItem = createAnimatorForItem(itemIndex, rootIndex, startOffset, finalItemOffset);
                animatorForItem.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        final float offset = (float) animation.getAnimatedValue();
                        ViewPortItem<ItemType> movedItem = item.withLeftAnchor(offset);
                        mItems.set(itemIndex, movedItem);
                        listener.onItemMoved(movedItem);
                    }
                });
                itemAnimators.add(animatorForItem);
            }
        });

        AnimatorSet flingAnimator = new AnimatorSet();
        flingAnimator.setDuration(FLING_DURATION);
        flingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        flingAnimator.playTogether(itemAnimators);
        return flingAnimator;
    }

    protected interface OnItem<ItemType> {
        void onItem(int itemIndex, ViewPortItem<ItemType> item);
    }

    protected abstract void traverseItemsList(OnItem<ItemType> updater);

    protected abstract float computeFinalItemOffset(float leftAnchor, float distanceToMove);

    protected abstract ValueAnimator createAnimatorForItem(int itemIndex, int rootIndex, float startOffset, float finalItemOffset);

    protected ValueAnimator createRootAnimator(float startOffset, float finalItemOffset) {
        return ValueAnimator.ofFloat(startOffset, finalItemOffset);
    }

}
