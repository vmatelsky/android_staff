package com.vlabs.androiweartest.wear.di;

import android.content.Context;

import com.vlabs.androiweartest.wear.connection.ConnectionManager;
import com.vlabs.androiweartest.wear.connection.ConnectionManagerImpl;
import com.vlabs.androiweartest.wear.WearFacade;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        complete = false,    // Here we enable object graph validation
        library = true,
//        addsTo = ApplicationScopeModule.class, // Important for object graph validation at compile time
        injects = {
                WearFacade.class
        }
)
public class WearScopeModule {

    private final ConnectionManager mConnectionManager;

    public WearScopeModule(final Context context) {
        mConnectionManager = new ConnectionManagerImpl(context);
    }

    @Provides
    @Singleton
    public ConnectionManager connectionManager() {
        return mConnectionManager;
    }

}
