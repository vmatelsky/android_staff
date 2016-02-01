package com.vlabs.androiweartest.job.connectivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.path.android.jobqueue.network.NetworkEventProvider;
import com.path.android.jobqueue.network.NetworkUtil;

public class ConnectivityModule implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        NetworkUtil,
        NetworkEventProvider {

    public static final String TAG = ConnectivityModule.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;

    private Listener mListener;

    public ConnectivityModule(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (ensurePlayAvailable(context)) {
            mGoogleApiClient.connect();
        }
    }

    private boolean ensurePlayAvailable(Context context) {
        try {
            final int servicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            if (servicesAvailable != ConnectionResult.SUCCESS) {
                Log.e(TAG, "Google Services not available due to: " + GooglePlayServicesUtil.getErrorString(servicesAvailable) + " can't start wear!");
                return false;
            }
            return true;
        } catch (final IllegalStateException ise) {
            return false;
        }
    }

    @Override
    public void onConnected(final Bundle bundle) {
        if (mListener != null) {
            mListener.onNetworkChange(true);
        }
    }

    @Override
    public void onConnectionSuspended(final int i) {
        if (mListener != null) {
            mListener.onNetworkChange(false);
        }
    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        final String errorMessage = connectionResult == null ? "unknown error" : connectionResult.getErrorMessage();
        Log.e(TAG, "Can't start wear because " + errorMessage);

        if (mListener != null) {
            mListener.onNetworkChange(false);
        }
    }

    @Override
    public void setListener(final Listener listener) {
        mListener = listener;
    }

    @Override
    public boolean isConnected(final Context context) {
        return mGoogleApiClient.isConnected();
    }

    public GoogleApiClient googleApiClient() {
        return mGoogleApiClient;
    }
}
