package com.vlabs.androiweartest.activity.launch.pages;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.wearcontract.WearExtras;

public class SearchPageFragment extends Fragment{

    public static SearchPageFragment newInstance(final String extraStationPath) {
        final Bundle bundle = new Bundle();
        bundle.putString(WearExtras.EXTRA_STATION_PATH, extraStationPath);
        final SearchPageFragment fragment = new SearchPageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_search_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView iconView = (ImageView) view.findViewById(R.id.icon_search);
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onIconViewPressed();
            }
        });
    }

    private void onIconViewPressed() {
        if (WearApplication.instance().connectionManager().isConnected()) {
            Toast.makeText(getActivity().getApplicationContext(), "SearchPageFragment pressed", Toast.LENGTH_SHORT).show();
        } else {
            showNoConnectionMsg();
        }
    }

    private void showNoConnectionMsg() {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), R.string.connect_to_perform_action, Toast.LENGTH_LONG).show();
        }
    }
}
