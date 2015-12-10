package com.vlabs.uigames.caorusel;

import android.app.Activity;
import android.support.v4.app.Fragment;

public interface CarouselContentProvider {

    int count();
    Fragment fragmentForPosition(Activity activity, int position);

}
