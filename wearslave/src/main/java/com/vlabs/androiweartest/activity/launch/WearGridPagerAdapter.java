package com.vlabs.androiweartest.activity.launch;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.FragmentGridPagerAdapter;

import com.vlabs.androiweartest.activity.launch.pages.ForYouPageFragment;
import com.vlabs.androiweartest.activity.launch.pages.MyStationsFragment;
import com.vlabs.androiweartest.activity.launch.pages.SearchPageFragment;
import com.vlabs.wearcontract.Data;

import java.util.Arrays;
import java.util.List;

public class WearGridPagerAdapter extends FragmentGridPagerAdapter {

    private final List<Fragment> mPages = Arrays.asList(ForYouPageFragment.newInstance(),
                                                        SearchPageFragment.newInstance(Data.PATH_STATIONS_SEARCH),
                                                        MyStationsFragment.newInstance());

    public WearGridPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getFragment(int row, int col) {
        return mPages.get(col);
    }

    @Override
    public long getFragmentId(int row, int column) {
        return column;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int row) {
        return mPages.size();
    }


}
