package com.codepath.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "tweet")
public class Tweet extends Model implements Serializable {
    // Define table fields
    @Column(name = "body")
    private String body;
    @Column(name = "uid")
    private long uid;
    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "user_id")
    private long userId;

    private User user;

    public Tweet() {
        super();
    }

    // Parse model from JSON
    public static Tweet fromJSON(JSONObject json) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = json.getString("text");
            tweet.uid = json.getLong("id");
            tweet.createdAt = json.getString("created_at");
            User user = User.fromJSON(json.getJSONObject("user"));
            tweet.user = user;
            tweet.userId = user.getUid();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = (JSONObject) jsonArray.get(i);
                Tweet tw = Tweet.fromJSON(tweetJson);
                tw.save();
                tweets.add(tw);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }

    public static Tweet getTweetWithLowestId(List<Tweet> tweets) {
        List<Tweet> newTweetList = new ArrayList<>(tweets);
        Collections.sort(newTweetList, new Comparator<Tweet>() {
            @Override
            public int compare(Tweet lhs, Tweet rhs) {
                if (lhs.getUid() < rhs.getUid()) {
                    return -1;
                }
                if (lhs.getUid() > rhs.getUid()) {
                    return 1;
                }
                return 0;
            }
        });
        return newTweetList.get(0);
    }

    // Record Finders
    public static SampleModel byId(long id) {
        return new Select().from(SampleModel.class).where("id = ?", id).executeSingle();
    }

    public static List<SampleModel> recentItems() {
        return new Select().from(SampleModel.class).orderBy("id DESC").limit("300").execute();
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }
}
