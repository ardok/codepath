package com.codepath.instagram.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.PhotoGridActivity;
import com.codepath.instagram.models.InstagramUser;

import java.util.ArrayList;

public class SearchUserResultsAdapter extends BaseSearchResultAdapter {
    ArrayList<InstagramUser> userArrayList;
    Activity activity;

    public SearchUserResultsAdapter(ArrayList<InstagramUser> userList, Activity act) {
        userArrayList = userList;
        activity = act;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_search_result_simple, parent, false);
        return new SearchResultViewHolder(view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PhotoGridActivity.class);
                intent.putExtra("type", "user");
                intent.putExtra("userId", (String) v.getTag());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        final InstagramUser instagramUser = userArrayList.get(holder.getLayoutPosition());
        holder.itemView.setTag(instagramUser.userId);
        holder.setAvatar(instagramUser.profilePictureUrl);
        holder.setTitle(instagramUser.userName);
        holder.setDescription(instagramUser.fullName);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
}
