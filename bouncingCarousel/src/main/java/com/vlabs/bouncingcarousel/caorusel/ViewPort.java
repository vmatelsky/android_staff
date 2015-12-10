package com.vlabs.bouncingcarousel.caorusel;

import android.animation.Animator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewPort<ItemType> {

    private final int mLeft;
    private final int mRight;

    private final ArrayList<ViewPortItem<ItemType>> mActiveItems = new ArrayList<>();

    public ViewPort(int left, int right) {
        mLeft = left;
        mRight = right;
    }

    public void positionate(ViewPortItem<ItemType> initial, int childWidth) {
        mActiveItems.clear();
        fillOnTheRight(mActiveItems, initial, childWidth);
        fillOnTheLeft(mActiveItems, initial, childWidth);
    }

    public ViewPortItem<ItemType> positionateForAppearance(InfiniteIterator<ItemType> infiniteIterator, int childWidth) {
        mActiveItems.clear();
        ViewPortItem<ItemType> initial = new ViewPortItem<>(mRight, infiniteIterator);
        mActiveItems.add(initial);
        return initial;
    }

    private void fillOnTheRight(List<ViewPortItem<ItemType>> itemsList, ViewPortItem<ItemType> initial, int childWidth) {
        while (initial.leftAnchor() < mRight) {
            itemsList.add(initial);
            initial = initial.next(childWidth);
        }
    }

    private void fillOnTheLeft(List<ViewPortItem<ItemType>> itemsList, ViewPortItem<ItemType> initial, int childWidth) {
        ViewPortItem<ItemType> toLeft = initial;
        while (toLeft.leftAnchor() > mLeft) {
            toLeft = toLeft.prev(childWidth);
            itemsList.add(0, toLeft);
        }
    }

    public void scroll(float delta, int childWidth) {
        for(int i = 0; i < mActiveItems.size(); ++i) {
            ViewPortItem<ItemType> item = mActiveItems.get(i);
            float newAnchor = item.leftAnchor() + delta;
            mActiveItems.set(i, item.withLeftAnchor(newAnchor));
        }

        if (!mActiveItems.isEmpty()) {
            ViewPortItem<ItemType> leftmostItem = mActiveItems.get(0);
            fillOnTheLeft(mActiveItems, leftmostItem, childWidth);

            ViewPortItem<ItemType> rightmostItem = mActiveItems.get(mActiveItems.size() - 1);
            fillOnTheRight(mActiveItems, rightmostItem.next(childWidth), childWidth);
        }

        adjustItems(childWidth);
    }

    private void adjustItems(int childWidth) {
        List<ViewPortItem<ItemType>> visibleItems = visibleItems(childWidth);

        Iterator<ViewPortItem<ItemType>> iterator = mActiveItems.iterator();
        while (iterator.hasNext()) {
            ViewPortItem<ItemType> item = iterator.next();
            if (!visibleItems.contains(item)) {
                iterator.remove();
            }
        }
    }

    public List<ViewPortItem<ItemType>> visibleItems(int childWidth) {
        List<ViewPortItem<ItemType>> filtered = new ArrayList<>();

        for (ViewPortItem<ItemType> item : mActiveItems) {
            if ((Float.compare(item.leftAnchor(), mRight) < 0)
                && (Float.compare(item.leftAnchor(), mLeft - childWidth) > 0)) {
                filtered.add(item);
            }
        }

        return filtered;
    }

    public Animator flingLeft(final ViewPortItem<ItemType> flingRoot, final float distanceToFling, final int childWidth, Fling.FlingListener<ItemType> flingListener) {
        expandItemsOnTheRight(expandItemsCount(distanceToFling, childWidth), childWidth);
        Fling<ItemType> fling = new FlingLeft<>(mActiveItems, childWidth);
        return performFling(flingRoot, distanceToFling, flingListener, fling, childWidth);
    }

    private int expandItemsCount(float distanceToFling, int childWidth) {
        return Math.round(distanceToFling / childWidth) + 1;
    }

    private Animator performFling(
            ViewPortItem<ItemType> flingRoot,
            float distanceToFling,
            Fling.FlingListener<ItemType> flingListener,
            Fling<ItemType> fling,
            final int childWidth) {
        Animator animator = fling.fling(flingRoot, distanceToFling, flingListener);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                adjustItems(childWidth);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                adjustItems(childWidth);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setInterpolator(new OvershootInterpolator(0.8f));
        animator.start();
        return animator;
    }

    public Animator flingRight(final ViewPortItem<ItemType> flingRoot, final float distanceToFling, final int childWidth, Fling.FlingListener<ItemType> flingListener) {
        expandItemsOnTheLeft(expandItemsCount(distanceToFling, childWidth), childWidth);
        Fling<ItemType> fling = new FlingRight<>(mActiveItems, childWidth);
        return performFling(flingRoot, distanceToFling, flingListener, fling, childWidth);
    }

    private void expandItemsOnTheRight(int count, int childWidth) {
        for (int i = 0; i < count; ++i) {
            ViewPortItem<ItemType> item = mActiveItems.get(mActiveItems.size() - 1);
            mActiveItems.add(item.next(childWidth));
        }
    }

    private void expandItemsOnTheLeft(int count, int childWidth) {
        for (int i = 0; i < count; ++i) {
            ViewPortItem<ItemType> item = mActiveItems.get(0);
            mActiveItems.add(0, item.prev(childWidth));
        }
    }
}
