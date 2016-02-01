package com.vlabs.androiweartest.di.component;

import com.vlabs.androiweartest.activity.launch.pages.ForYouPageFragment;
import com.vlabs.androiweartest.activity.launch.pages.MyStationsFragment;
import com.vlabs.androiweartest.activity.launch.pages.SearchPageFragment;
import com.vlabs.androiweartest.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface FragmentComponent {

    void inject(ForYouPageFragment forYouPageFragment);

    void inject(MyStationsFragment myStationsFragment);

    void inject(SearchPageFragment searchPageFragment);
}
