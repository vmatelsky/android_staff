package com.vlabs.uigames;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.vlabs.uigames.caorusel.AdapterInfiniteBehavior;
import com.vlabs.uigames.caorusel.CarouselPagerAdapter;
import com.vlabs.uigames.provider.PagesContentProvider;
import com.vlabs.uigames.transformers.ZoomOutPageTransformer;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager mainPager = (ViewPager) findViewById(R.id.main_pager);
        mainPager.setPageTransformer(true, new ZoomOutPageTransformer());
        final CarouselPagerAdapter adapter = new CarouselPagerAdapter(
                this,
                getSupportFragmentManager(),
                new PagesContentProvider());

        AdapterInfiniteBehavior.apply(mainPager, adapter);
    }

}
