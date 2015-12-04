package com.codepath.instagram.networking;

import android.content.Context;

import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Constants;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;

public class InstagramClient extends OAuthBaseClient {
    public static final String REST_URL = "https://api.instagram.com/v1";
    public static final Class<? extends Api> REST_API_CLASS = InstagramApi.class;
    public static final String REST_CONSUMER_KEY = "e05c462ebd86446ea48a5af73769b602";
    public static final String REST_CONSUMER_SECRET = "7f18a14de6c241c2a9ccc9f4a3df4b35";
    public static final String REDIRECT_URI = Constants.REDIRECT_URI;
    public static final String SCOPE = Constants.SCOPE;

    public InstagramClient(Context c) {
        super(c, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REDIRECT_URI, SCOPE);
    }

    public static InstagramClient getInstance() {
        return MainApplication.getRestClient();
    }

    public void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        client.get(getApiUrl("media/popular"), responseHandler);
    }

    public void getSelfFeed(JsonHttpResponseHandler responseHandler) {
        client.get(getApiUrl("users/self/feed"), responseHandler);
    }

    public void getComments(String mediaId, JsonHttpResponseHandler responseHandler) {
        client.get(getApiUrl(String.format("media/%s/comments", mediaId)), responseHandler);
    }

    public void searchUser(String q, JsonHttpResponseHandler responseHandler) {
        RequestParams requestParams = new RequestParams();
        requestParams.add("q", q);
        client.get(getApiUrl("users/search"), requestParams, responseHandler);
    }

    public void searchTag(String q, JsonHttpResponseHandler responseHandler) {
        RequestParams requestParams = new RequestParams();
        requestParams.add("q", q);
        client.get(getApiUrl("tags/search"), requestParams, responseHandler);
    }

    public void getUserMediaRecent(String userId, JsonHttpResponseHandler responseHandler) {
        client.get(getApiUrl(String.format("users/%s/media/recent/", userId)), responseHandler);
    }

    public void getTagMediaRecent(String tagName, JsonHttpResponseHandler responseHandler) {
        client.get(getApiUrl(String.format("tags/%s/media/recent/", tagName)), responseHandler);
    }

    public String getAccessToken() {
        return client.getAccessToken().getToken();
    }
}
