package com.codepath.instagram.core;

import android.app.Application;

public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    private static MainApplication instance;

    private static final String clientId = "e05c462ebd86446ea48a5af73769b602";

    public static MainApplication sharedApplication() {
        assert(instance != null);
        return instance;
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
