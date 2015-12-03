package com.codepath.instagram.core;

import android.app.Application;

import com.codepath.instagram.networking.InstagramClient;

public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    private static MainApplication instance;

    private static final String clientId = "e05c462ebd86446ea48a5af73769b602";

    public static MainApplication sharedApplication() {
        assert(instance != null);
        return instance;
    }

    public static String getClientId() {
        return clientId;
    }

    public static InstagramClient getRestClient() {
        return (InstagramClient) InstagramClient.getInstance(InstagramClient.class, sharedApplication());
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
