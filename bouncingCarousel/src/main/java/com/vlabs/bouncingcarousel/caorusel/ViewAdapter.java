package com.vlabs.bouncingcarousel.caorusel;

import android.view.View;
import android.view.ViewGroup;

public interface ViewAdapter<ItemType> {

    View createView(ViewGroup parent);
    void bindItem(View view, ItemType item);
}
