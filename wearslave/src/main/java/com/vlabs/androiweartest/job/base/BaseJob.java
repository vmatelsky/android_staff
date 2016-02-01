package com.vlabs.androiweartest.job.base;

import android.net.Uri;
import android.support.annotation.IntDef;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.vlabs.androiweartest.di.component.AppComponent;
import com.vlabs.androiweartest.job.connectivity.ConnectivityModule;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public abstract class BaseJob extends Job {

    protected static final long ASSET_TIMEOUT = 10;

    @Inject
    transient ConnectivityModule mConnectivityModule;

    @Inject
    transient EventBus mEventBus;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({UI_HIGH, BACKGROUND})
    public @interface Priority {

    }

    public static final int UI_HIGH = 10;
    public static final int BACKGROUND = 1;

    public BaseJob() {
        super(new Params(BACKGROUND).requireNetwork());
    }

    public void inject(AppComponent appComponent) {
        appComponent.inject(this);
    }

    protected List<Node> connectedNodes() {
        return Wearable.NodeApi.getConnectedNodes(googleApiClient()).await().getNodes();
    }

    protected GoogleApiClient googleApiClient() {
        return mConnectivityModule.googleApiClient();
    }

    protected EventBus eventBus() {
        return mEventBus;
    }

    protected static Uri dataUriForNode(Node node) {
        return new Uri.Builder().scheme(PutDataRequest.WEAR_URI_SCHEME).authority(node.getId()).build();
    }

    protected static Node getMainNodeSync(final GoogleApiClient apiClient) {
        NodeApi.GetConnectedNodesResult nodeResult = Wearable.NodeApi.getConnectedNodes(apiClient).await();
        if (nodeResult.getNodes().isEmpty()) {
            return null;
        } else {
            return nodeResult.getNodes().get(0);
        }
    }

}
