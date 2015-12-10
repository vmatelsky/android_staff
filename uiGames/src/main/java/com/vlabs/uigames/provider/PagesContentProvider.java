package com.vlabs.uigames.provider;


import android.app.Activity;
import android.support.v4.app.Fragment;

import com.vlabs.uigames.caorusel.CarouselContentProvider;
import com.vlabs.uigames.functional.Getter;
import com.vlabs.uigames.pages.MainFragment;
import com.vlabs.uigames.pages.PlayerFragment;
import com.vlabs.uigames.pages.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class PagesContentProvider implements CarouselContentProvider {

    private final List<Getter<Fragment>> mPageGetters = new ArrayList<>();

    public PagesContentProvider() {
        mPageGetters.add(new Getter<Fragment>() {
            @Override
            public Fragment get() {
                return MainFragment.newInstance();
            }
        });
        mPageGetters.add(new Getter<Fragment>() {
            @Override
            public Fragment get() {
                return PlayerFragment.newInstance();
            }
        });
        mPageGetters.add(new Getter<Fragment>() {
            @Override
            public Fragment get() {
                return SettingsFragment.newInstance();
            }
        });
    }

    @Override
    public int count() {
        return mPageGetters.size();
    }

    @Override
    public Fragment fragmentForPosition(Activity activity, int position) {
        return mPageGetters.get(position).get();
    }

}
