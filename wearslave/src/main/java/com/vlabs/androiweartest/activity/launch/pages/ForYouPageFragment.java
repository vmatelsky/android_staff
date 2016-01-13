package com.vlabs.androiweartest.activity.launch.pages;

import android.app.Activity;
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
import com.vlabs.androiweartest.activity.pick.PickStationActivity;
import com.vlabs.wearcontract.Data;

public class ForYouPageFragment extends Fragment {
    private static final int REQUEST_PICK = 1;

    public ForYouPageFragment() {}

    public static ForYouPageFragment newInstance() {
        return new ForYouPageFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_for_you_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageView iconView = (ImageView) view.findViewById(R.id.icon_for_you);
        iconView.setOnClickListener(v -> onIconViewPressed());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isAdded() && requestCode == REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            getActivity().finish();
        }
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
        startActivityForResult(intent, REQUEST_PICK);
    }

    private Intent createPickStationIntent() {
        return PickStationActivity.createLaunchIntent(getActivity(), Data.PATH_STATIONS_FOR_YOU, getString(R.string.no_stations_for_you));
    }

    private void showNoConnectionMsg() {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), R.string.connect_to_perform_action, Toast.LENGTH_LONG).show();
        }
    }
}
