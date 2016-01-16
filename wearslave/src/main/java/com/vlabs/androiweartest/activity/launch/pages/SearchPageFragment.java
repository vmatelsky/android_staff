package com.vlabs.androiweartest.activity.launch.pages;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.activity.search.SearchActivity;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearExtras;

import java.util.ArrayList;

public class SearchPageFragment extends Fragment{

    private static final int REQUEST_SPEECH = 1;

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

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SPEECH) {

            if (resultCode == Activity.RESULT_CANCELED) return;

            if (resultCode == Activity.RESULT_OK) {
                final ArrayList<String> terms = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (terms.isEmpty()) {
                    showToast(getString(R.string.search_no_result));
                } else {
                    WearApplication.instance().analytics().broadcastRemoteControl(WearAnalyticsConstants.WearBrowse.VOICE_SEARCH);
                    searchFor(terms.get(0));
                }
            }
        }
    }

    private void onIconViewPressed() {
        if (WearApplication.instance().connectionManager().isConnected()) {
            startVoiceRecognition();
        } else {
            showNoConnectionMsg();
        }
    }

    public void showToast(final String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void searchFor(final String term) {
        final Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra(WearExtras.EXTRA_QUERY, term);
        getActivity().startActivity(intent);
    }

    private void showNoConnectionMsg() {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), R.string.connect_to_perform_action, Toast.LENGTH_LONG).show();
        }
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say e.g. Rihanna or Z100");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, REQUEST_SPEECH);
    }
}
