package com.codepath.instagram.networking;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class InstagramClient {
    public static void getPopularFeed(String clientId, JsonHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format("https://api.instagram.com/v1/media/popular?client_id=%s", clientId), responseHandler);
    }

    public static void getComments(String mediaId, String clientId, JsonHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format("https://api.instagram.com/v1/media/%s/comments?client_id=%s", mediaId, clientId), responseHandler);
    }
}
