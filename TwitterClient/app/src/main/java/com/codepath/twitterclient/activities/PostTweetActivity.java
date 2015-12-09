package com.codepath.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.twitterclient.R;
import com.codepath.twitterclient.core.TwitterApplication;
import com.codepath.twitterclient.helpers.ImageUtils;
import com.codepath.twitterclient.models.Tweet;
import com.codepath.twitterclient.models.User;
import com.codepath.twitterclient.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PostTweetActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_POST_TWEET = 1000;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.userAvatarIV)
    ImageView userAvatarIV;
    @Bind(R.id.userFullNameTV)
    TextView userFullNameTV;
    @Bind(R.id.userScreenNameTV)
    TextView userScreenNameTV;
    @Bind(R.id.tweetBodyET)
    EditText tweetBodyET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tweet);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        User sessionUser = TwitterApplication.getSessionUser();

        ImageUtils.setPicassoImage(userAvatarIV, sessionUser.getProfileImageUrl());
        userFullNameTV.setText(sessionUser.getName());
        userScreenNameTV.setText(String.format("@%s", sessionUser.getScreenName()));
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // TODO use something other than `onBackPressed`
                super.onBackPressed();
                return true;

            case R.id.action_post_tweet:
                postTweet();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void postTweet() {
        String tweetBody = tweetBodyET.getText().toString();
        if (tweetBody.trim().isEmpty()) {
            Toast.makeText(this, "Can't tweet empty text", Toast.LENGTH_SHORT).show();
            return;
        }
        TwitterClient.getInstance().postTweet(tweetBody, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet newTweet = Tweet.fromJSON(response);
                newTweet.save();
                Intent intent = new Intent();
                intent.putExtra("tweet", newTweet);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }
}
