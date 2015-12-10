package com.vlabs.bouncingcarousel.caorusel;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.vlabs.bouncingcarousel.Maybe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InfiniteScroller<ItemType> extends ViewGroup implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        Fling.FlingListener<ItemType> {

    private static final int FLING_ITEMS_COUNT = 4;
    private static final float VISIBLE_ITEMS_COUNT = 4.5f;
    private InfiniteIterator<ItemType> mInitialItem;
    private ViewAdapter<ItemType> mViewAdapter;

    private InfiniteScrollerConfig mConfig = new InfiniteScrollerConfig(0);

    private final GestureDetector mGestureDetector;

    private int mChildWidth;
    private int mChildHeightSpec;
    private int mChildWidthSpec;
    private boolean mLayoutInProgress;
    private boolean mIsDirty;

    private ViewPort<ItemType> mViewPort;
    private Animator mFlingAnimator;

    public InfiniteScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, this);
        mGestureDetector.setIsLongpressEnabled(false);
        setOnTouchListener(this);
    }

    public void setAdapter(ViewAdapter<ItemType> adapter, InfiniteIterator<ItemType> initialItem) {
        mInitialItem = initialItem;
        mViewAdapter = adapter;
        mIsDirty = true;
        requestLayout();
    }

    public float visibleChildrenCount() {
        return VISIBLE_ITEMS_COUNT;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        mChildHeightSpec = heightSpec(getMeasuredHeight());

        int measureWidth = getMeasuredWidth();
        mChildWidth = Math.round(measureWidth / visibleChildrenCount());
        mChildWidthSpec = widthSpec(mChildWidth);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!mLayoutInProgress && mIsDirty) {
            Log.d("layout_debug", "process onLayout");
            layoutChildren();
        } else {
            Log.d("layout_debug", "skip onLayout");
        }
    }

    public void layoutChildren() {
        if (mInitialItem == null) {
            return;
        }

        mLayoutInProgress = true;

        if (mViewPort == null) {
            mViewPort = new ViewPort<>(getLeft(), getRight());
            mViewPort.positionate(new ViewPortItem<>(0, mInitialItem), mChildWidth);
        }

        List<View> children = children();

        List<ViewPortItem<ItemType>> items = mViewPort.visibleItems(mChildWidth);
        Iterator<ViewPortItem<ItemType>> iterator = items.iterator();

        while (iterator.hasNext()) {
            ViewPortItem<ItemType> viewPortItem = iterator.next();

            View view = viewForItem(children, viewPortItem);
            if (view != null) {
                children.remove(view);
                view.setTranslationX(viewPortItem.leftAnchor());
                iterator.remove();
            }
        }

        iterator = items.iterator();
        while (iterator.hasNext()) {
            ViewPortItem<ItemType> viewPortItem = iterator.next();

            if (children.isEmpty()) {

                final int elementsStartOffset = mConfig.leftOffset();

                final View child = mViewAdapter.createView(this);
                child.measure(mChildWidthSpec, mChildHeightSpec);

                child.layout(elementsStartOffset,
                             0,
                             mChildWidth + elementsStartOffset,
                             child.getMeasuredHeight());

                child.setTag(viewPortItem);
                child.setTranslationX(viewPortItem.leftAnchor());
                mViewAdapter.bindItem(child, viewPortItem.item());
                addView(child);
            } else {
                View child = children.get(0);
                children.remove(0);
                child.setTag(viewPortItem);
                child.setTranslationX(viewPortItem.leftAnchor());
                mViewAdapter.bindItem(child, viewPortItem.item());
            }
        }

        for (View child : children) {
            removeView(child);
        }

        mLayoutInProgress = false;
        mIsDirty = false;
    }

    private View viewForItem(List<View> children, ViewPortItem<ItemType> viewPortItem) {

        for (View child : children) {
            Object tag = child.getTag();
            if (tag instanceof ViewPortItem) {
                if (viewPortItem.equals((ViewPortItem)tag)) {
                    return child;
                }
            }
        }

        return null;
    }

    private ItemType toItem(final View child) {
        return ((ViewPortItem<ItemType>)child.getTag()).item();
    }

    private int widthSpec(int width) {
        return MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
    }

    private int heightSpec(int height) {
        return MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("layout_debug", "scroll for " + -distanceX);
//        mViewPort.scroll(-distanceX, mChildWidth);
//        mIsDirty = true;
//        requestLayout();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    /**
     * e1 	        The first down motion event that started the fling.
     * e2 	        The move motion event that triggered the current onFling.
     * velocityX 	The velocity of this fling measured in pixels per second along the x axis.
     * velocityY 	The velocity of this fling measured in pixels per second along the y axis.
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        final Maybe<View> item = findChildUnderMotion(e2);
        Log.d("layout_debug", "fling");

        if (mFlingAnimator != null) {
            if (mFlingAnimator.isRunning() || mFlingAnimator.isStarted()) {
                return true;
            }
        }

        if (item.isDefined()) {
            View targetView = item.get();

            ViewPortItem<ItemType> itemTag = (ViewPortItem<ItemType>) targetView.getTag();

            if (isFlingToRight(velocityX)) {
                mFlingAnimator = mViewPort.flingRight(itemTag, FLING_ITEMS_COUNT * mChildWidth, mChildWidth, this);
            } else {
                mFlingAnimator = mViewPort.flingLeft(itemTag, FLING_ITEMS_COUNT * mChildWidth, mChildWidth, this);
            }
        }

        return true;
    }

    @Override
    public void onItemMoved(ViewPortItem<ItemType> item) {
        mIsDirty = true;
        requestLayout();
    }

    private List<View> children() {
        List<View> children = new ArrayList<>();

        final int childCount = getChildCount();
        for(int i = 0; i < childCount; ++i) {
            children.add(getChildAt(i));
        }

        return children;
    }

    private boolean isFlingToRight(float velocityX) {
        return velocityX > 0;
    }

    private Maybe<View> findChildUnderMotion(MotionEvent motionEvent) {
        final int childCount = getChildCount();
        final float motionCoordX = motionEvent.getX();

        for(int i = 0; i < childCount; ++i) {
            View child = getChildAt(i);

            final float childLeftBorder = child.getTranslationX();
            final float childRightBorder = childLeftBorder + mChildWidth;

            if ((motionCoordX > childLeftBorder) && (motionCoordX < childRightBorder)) {
                return Maybe.nullIsNothing(child);
            }
        }

        return Maybe.nothing();
    }

    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

}
