package com.codepath.twitterclient.networking;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.codepath.twitterclient.core.TwitterApplication;
import com.codepath.twitterclient.helpers.Constants;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

public class TwitterClient extends OAuthBaseClient {
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_CONSUMER_KEY = "SSsej3LaUywBJEgLOLy4d57gk";
    public static final String REST_CONSUMER_SECRET = "ZFQDBmuHRxyXQ4M7zZZuGnBb3OV1qQbq9mvGSOXZIYGPaM967t";
    public static final String REDIRECT_URI = Constants.REDIRECT_URI;

    public TwitterClient(Context c) {
        super(c, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REDIRECT_URI);
    }

    public static TwitterClient getInstance() {
        return TwitterApplication.getRestClient();
    }

    public void getHomeTimeline(int count, long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", String.valueOf(count));
        if (maxId > 0) {
            params.put("max_id", maxId);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getCurrentUser(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, handler);
    }

    public void postTweet(String tweetBody, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweetBody);
        getClient().post(apiUrl, params, handler);
    }

//    private Header getAuthorizationHeader() {
//        // Add Authorization header
//        // https://dev.twitter.com/oauth/overview/authorizing-requests
//        final StringBuilder authorizationHeader = new StringBuilder();
//
//        authorizationHeader.append("OAuth ");
//
//        // 1
//        authorizationHeader.append("oauth_consumer_key=\"");
//        authorizationHeader.append(REST_CONSUMER_KEY);
//        authorizationHeader.append("\"");
//
//        // 2
//        authorizationHeader.append(", oauth_nonce=\"");
//        byte[] nonceBytes = new byte[32];
//        new SecureRandom().nextBytes(nonceBytes);
//        authorizationHeader.append(new StringUtils(Base64.encode(nonceBytes, Base64.URL_SAFE)).replaceAll("[^\\p{L}\\p{Nd}]+", ""));
//        authorizationHeader.append("\"");
//
//        // 3
//        authorizationHeader.append(", oauth_signature=\"");
//
//        // 4
//        authorizationHeader.append(", oauth_signature_method=\"HMAC-SHA1\"");
//
//        // 5
//        authorizationHeader.append(", oauth_timestamp=\"");
//        Long tsLong = System.currentTimeMillis()/1000;
//        authorizationHeader.append(tsLong.toString());
//        authorizationHeader.append("\"");
//
//        // 6
//        authorizationHeader.append("&oauth_token=\"");
//        authorizationHeader.append(client.getAccessToken());
//        authorizationHeader.append("\"");
//
//        // 7
//        authorizationHeader.append("&oauth_version=\"1.0\"");
//
//        return new Header() {
//            @Override
//            public StringUtils getName() {
//                return "Authorization";
//            }
//
//            @Override
//            public StringUtils getValue() {
//                return authorizationHeader.toString();
//            }
//
//            @Override
//            public HeaderElement[] getElements() throws ParseException {
//                return new HeaderElement[0];
//            }
//        };
//    }
}
