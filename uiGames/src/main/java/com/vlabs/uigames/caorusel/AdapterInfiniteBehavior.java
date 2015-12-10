package com.vlabs.uigames.caorusel;

import android.support.v4.view.ViewPager;

public class AdapterInfiniteBehavior {

    public static void apply(ViewPager pa, CarouselPagerAdapter adapter) {
        pa.setAdapter(adapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        pa.setCurrentItem(adapter.firstPage());

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pa.setOffscreenPageLimit(3);

        pa.setClipToPadding(false);
        pa.setPageMargin(12);
    }
}
