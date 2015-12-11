package com.codepath.twitterclient.core;

import android.app.Application;
import android.content.Context;
import android.view.View;

import com.codepath.twitterclient.models.User;
import com.codepath.twitterclient.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     RestClient client = TwitterApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TwitterApplication extends Application {
    private static Context context;
    private static User sessionUser;
    private static TwitterClient clientInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterApplication.context = this;
    }

    public static TwitterClient getRestClient() {
        if (clientInstance == null) {
            clientInstance = (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterApplication.context);
        }
        return clientInstance;
    }

    public static Context getContext() {
        return context;
    }

    public static void setSessionUser(User user) {
        sessionUser = user;
    }

    public static User getSessionUser() {
        return sessionUser;
    }
}
