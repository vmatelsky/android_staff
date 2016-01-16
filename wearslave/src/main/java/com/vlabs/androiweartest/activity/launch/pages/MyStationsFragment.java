package com.vlabs.androiweartest.activity.launch.pages;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.activity.launch.WearMainActivity;
import com.vlabs.androiweartest.activity.pick.PickStationActivity;
import com.vlabs.wearcontract.Data;

public class MyStationsFragment extends Fragment {

    public static MyStationsFragment newInstance() {
        return new MyStationsFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_my_stations_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView iconView = (ImageView) view.findViewById(R.id.icon_my_stations);
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onIconViewPressed();
            }
        });
    }

    protected void onIconViewPressed() {
        if (WearApplication.instance().connectionManager().isConnected()) {
            launchStationList();
        } else {
            showNoConnectionMsg();
        }
    }

    protected void launchStationList() {
        launchPickStation(createPickStationIntent());
    }

    private void launchPickStation(Intent intent) {
        startActivityForResult(intent, WearMainActivity.REQUEST_PICK);
    }

    private Intent createPickStationIntent() {
        return PickStationActivity.createLaunchIntent(getActivity(), Data.PATH_STATIONS_MY_STATIONS, getString(R.string.no_stations_my_stations));
    }

    private void showNoConnectionMsg() {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), R.string.connect_to_perform_action, Toast.LENGTH_LONG).show();
        }
    }

}
