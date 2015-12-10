package com.vlabs.bouncingcarousel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vlabs.bouncingcarousel.caorusel.InfiniteIteratorImpl;
import com.vlabs.bouncingcarousel.caorusel.InfiniteScroller;
import com.vlabs.bouncingcarousel.caorusel.ViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final ViewAdapter<CarouselItem> ADAPTER = new ViewAdapter<CarouselItem>() {
        @Override
        public View createView(final ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_item_layout, parent, false);
        }

        @Override
        public void bindItem(final View view, final CarouselItem item) {
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(item.text());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<CarouselItem> data = new ArrayList<>();
        data.add(new CarouselItem("some text"));
        data.add(new CarouselItem("some text"));
        data.add(new CarouselItem("some text"));
        data.add(new CarouselItem("some text"));
        data.add(new CarouselItem("some text"));
        data.add(new CarouselItem("some text"));
        data.add(new CarouselItem("some text"));


        final InfiniteScroller<CarouselItem> scroller = (InfiniteScroller<CarouselItem>) findViewById(R.id.infinity_scroller);
        scroller.setAdapter(ADAPTER, new InfiniteIteratorImpl<>(data));
    }

}
