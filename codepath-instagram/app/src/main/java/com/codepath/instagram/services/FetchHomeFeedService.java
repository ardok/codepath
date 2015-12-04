package com.codepath.instagram.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPostsWrapper;
import com.codepath.instagram.networking.InstagramClient;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FetchHomeFeedService extends IntentService {
    public static final String ACTION = "com.codepathinstagram.services.FetchHomeFeedService";

    private AsyncHttpClient aClient = new SyncHttpClient();

    public FetchHomeFeedService() {
        super("FetchHomeFeedService");
    }

    private void broadcastFailureIntent(int statusCode, Header[] headers, JSONObject errorResponse) {
        Intent in = new Intent(ACTION);
        in.putExtra("resultCode", statusCode);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
    }

    private void broadcastFailureIntent(int statusCode, Header[] headers, String responseString) {
        Intent in = new Intent(ACTION);
        in.putExtra("resultCode", statusCode);
        in.putExtra("resultValue", responseString);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Time to fetch data
        final String accessToken = intent.getStringExtra("accessToken");
        if (accessToken == null) {
            broadcastFailureIntent(502, null, "No access token");
            return;
        }

        final Context c = getApplicationContext();
        final InstagramClientDatabase icd = InstagramClientDatabase.getInstance(c);

        RequestParams rp = new RequestParams();
        rp.add("access_token", accessToken);
        aClient.get(InstagramClient.REST_URL + "/users/self/feed", rp, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // No need to do anything
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject meta = response.getJSONObject("meta");
                    if (meta.getInt("code") != 200) {
                        return;
                    }
                    ArrayList<InstagramPost> instagramPostList = (ArrayList<InstagramPost>) Utils.decodePostsFromJsonResponse(response);

                    // Store the latest into database
                    icd.emptyAllTables();
                    icd.addInstagramPosts(instagramPostList);

                    Intent in = new Intent(ACTION);
                    in.putExtra("resultCode", 200);
                    in.putExtra("resultValue", new InstagramPostsWrapper(instagramPostList));
                    LocalBroadcastManager.getInstance(c).sendBroadcast(in);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                broadcastFailureIntent(statusCode, headers, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                broadcastFailureIntent(statusCode, headers, responseString);
            }
        });
    }
}
