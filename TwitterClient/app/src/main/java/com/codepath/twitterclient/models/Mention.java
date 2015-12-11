package com.codepath.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Table(name = "mention")
public class Mention extends Model implements Serializable {
    @Column(name = "uid")
    private long uid;
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "text")
    private String text;
    @Column(name = "retweeted")
    private boolean retweeted;

    private User user;

    // Parse model from JSON
    public static Mention fromJSON(JSONObject json) {
        Mention mention = new Mention();
        try {
            mention.text = json.getString("text");
            mention.uid = json.getLong("id");
            mention.createdAt = json.getString("created_at");
            User user = User.fromJSON(json.getJSONObject("user"));
            mention.user = user;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mention;
    }

    public static ArrayList<Mention> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Mention> mentions = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject mentionJson = (JSONObject) jsonArray.get(i);
                Mention mention = Mention.fromJSON(mentionJson);
                mention.save();
                mentions.add(mention);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mentions;
    }

    public static Mention getMentionWithLowestId(List<Mention> mentions) {
        List<Mention> newMentionList = new ArrayList<>(mentions);
        Collections.sort(newMentionList, new Comparator<Mention>() {
            @Override
            public int compare(Mention lhs, Mention rhs) {
                if (lhs.getUid() < rhs.getUid()) {
                    return -1;
                }
                if (lhs.getUid() > rhs.getUid()) {
                    return 1;
                }
                return 0;
            }
        });
        return newMentionList.get(0);
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public User getUser() {
        return user;
    }
}
