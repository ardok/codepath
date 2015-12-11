package com.codepath.twitterclient.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.twitterclient.R;
import com.codepath.twitterclient.activities.ProfileActivity;
import com.codepath.twitterclient.helpers.ImageUtils;
import com.codepath.twitterclient.helpers.StringUtils;
import com.codepath.twitterclient.models.Mention;
import com.codepath.twitterclient.models.User;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MentionTimelineAdapter extends RecyclerView.Adapter<MentionTimelineAdapter.MentionTimelineViewHolder> {
    private ArrayList<Mention> mentionList;
    private Activity activity;

    public MentionTimelineAdapter(Activity activity,
                                  ArrayList<Mention> mentionList) {
        this.activity = activity;
        this.mentionList = mentionList;
    }

    @Override
    public MentionTimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mention_timeline, parent, false);
        return new MentionTimelineViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(MentionTimelineViewHolder holder, int position) {
        final Mention mention = mentionList.get(holder.getLayoutPosition());
        final User user = mention.getUser();
        holder.setTweeterAvatar(user.getProfileImageUrl());
        holder.tweeterAvatarIV.setTag(user.getUid());
        holder.setTweeterFullName(user.getName());
        holder.setTweeterScreenName(user.getScreenName());
        holder.setTweetBody(mention.getText());
        holder.setTweetDate(mention.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return mentionList.size();
    }

    static class MentionTimelineViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tweeterAvatarIV) ImageView tweeterAvatarIV;
        @Bind(R.id.tweeterFullNameTV) TextView tweeterFullNameTV;
        @Bind(R.id.tweeterScreenNameTV) TextView tweeterScreenNameTV;
        @Bind(R.id.tweetBodyTV) TextView tweetBodyTV;
        @Bind(R.id.tweetDateTV) TextView tweetDateTV;

        public MentionTimelineViewHolder(View itemView, final Activity activity) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tweeterAvatarIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("userId", (long) v.getTag());
                    Pair<View, String> p1 = Pair.create((View) tweeterAvatarIV, "tweeterAvatarIV");
                    Pair<View, String> p2 = Pair.create((View) tweeterFullNameTV, "tweeterFullNameTV");
                    Pair<View, String> p3 = Pair.create((View) tweeterScreenNameTV, "tweeterScreenNameTV");
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1, p2, p3);
                    activity.startActivity(intent, options.toBundle());
                }
            });
        }

        public void setTweeterAvatar(String url) {
            ImageUtils.setPicassoImage(tweeterAvatarIV, url);
        }

        public void setTweeterFullName(String name) {
            tweeterFullNameTV.setText(name);
        }

        public void setTweeterScreenName(String name) {
            tweeterScreenNameTV.setText(String.format("@%s", name));
        }

        public void setTweetBody(String tweetBody) {
            tweetBodyTV.setText(tweetBody);
        }

        public void setTweetDate(String rawDate) {
            tweetDateTV.setText(StringUtils.getRelativeTimeAgo(rawDate));
        }
    }
}
