package com.vlabs.bouncingcarousel.caorusel;

public final class ViewPortItem<ItemType> {

    private final float mLeftAnchor;
    private final InfiniteIterator<ItemType> mIterator;

    public ViewPortItem(float leftAnchor, InfiniteIterator<ItemType> iterator) {
        this.mLeftAnchor = leftAnchor;
        this.mIterator = iterator;
    }

    public ViewPortItem<ItemType> withLeftAnchor(float leftAnchor) {
        return new ViewPortItem<>(leftAnchor, mIterator);
    }

    public ViewPortItem<ItemType> next(int childWidth) {
        return new ViewPortItem<>(mLeftAnchor + childWidth, mIterator.next());
    }

    public ViewPortItem<ItemType> prev(int childWidth) {
        return new ViewPortItem<>(mLeftAnchor - childWidth, mIterator.previous());
    }

    public ItemType item() {
        return mIterator.item();
    }

    public float leftAnchor() {
        return mLeftAnchor;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ViewPortItem))return false;
        ViewPortItem otherMyClass = (ViewPortItem)other;
        return mIterator.equals(otherMyClass.mIterator);
    }
}
