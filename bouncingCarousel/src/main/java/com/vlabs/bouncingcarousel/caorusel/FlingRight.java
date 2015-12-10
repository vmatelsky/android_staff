package com.vlabs.bouncingcarousel.caorusel;

import android.animation.ValueAnimator;

import java.util.List;

public class FlingRight<ItemType> extends Fling<ItemType> {

    public FlingRight(List<ViewPortItem<ItemType>> viewPortItems, int itemWidth) {
        super(viewPortItems, itemWidth);
    }

    @Override
    protected void traverseItemsList(OnItem<ItemType> updater) {
        for(int i = mItems.size() - 1; i >= 0; --i) {
            updater.onItem(i, mItems.get(i));
        }
    }

    @Override
    protected float computeFinalItemOffset(float leftAnchor, float distanceToMove) {
        return leftAnchor + distanceToMove;
    }

    @Override
    protected ValueAnimator createAnimatorForItem(int itemIndex, int rootIndex, float startOffset, float finalItemOffset) {
        ValueAnimator animator = createRootAnimator(startOffset, finalItemOffset);

        if (itemIndex < rootIndex) {
            final int dIndex = Math.abs(itemIndex - rootIndex);
            final int startDelay = dIndex * 50;
            animator.setDuration(FLING_DURATION - startDelay);
            animator.setStartDelay(startDelay);
        }

        return animator;
    }
}
