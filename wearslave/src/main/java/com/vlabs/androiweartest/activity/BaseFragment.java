package com.vlabs.androiweartest.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.di.component.DaggerFragmentComponent;
import com.vlabs.androiweartest.di.component.FragmentComponent;

public class BaseFragment extends Fragment {

    private FragmentComponent mComponent;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerFragmentComponent.builder()
                .appComponent(app().appComponent()).build();
    }

    protected WearApplication app() {
        return WearApplication.instance();
    }

    protected FragmentComponent getComponent() {
        return mComponent;
    }
}
