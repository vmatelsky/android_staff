package com.vlabs.androiweartest.activity.launch.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.activity.BaseFragment;
import com.vlabs.androiweartest.activity.pick.PickStationActivity;
import com.vlabs.androiweartest.job.connectivity.ConnectivityModule;
import com.vlabs.wearcontract.WearDataEvent;

import javax.inject.Inject;

public class MyStationsFragment extends BaseFragment {

    public static MyStationsFragment newInstance() {
        return new MyStationsFragment();
    }

    @Inject
    ConnectivityModule mConnectivityModule;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_my_stations_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView iconView = (ImageView) view.findViewById(R.id.icon_my_stations);
        iconView.setOnClickListener(this::onIconViewPressed);
    }

    private void onIconViewPressed(final View view) {
        if (mConnectivityModule.isConnected(view.getContext())) {
            launchStationList();
        } else {
            showNoConnectionMsg();
        }
    }

    private void launchStationList() {
        launchPickStation(createPickStationIntent());
    }

    private void launchPickStation(Intent intent) {
        startActivity(intent);
    }

    private Intent createPickStationIntent() {
        return PickStationActivity.createIntent(getActivity(), WearDataEvent.PATH_STATIONS_MY_STATIONS, getString(R.string.no_stations_my_stations));
    }

    private void showNoConnectionMsg() {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), R.string.connect_to_perform_action, Toast.LENGTH_LONG).show();
        }
    }

}
