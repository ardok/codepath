package com.codepath.instagram.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.PhotoGridActivity;
import com.codepath.instagram.models.InstagramSearchTag;

import java.util.ArrayList;

public class SearchTagResultsAdapter extends BaseSearchResultAdapter {
    ArrayList<InstagramSearchTag> searchTagArrayList;
    Activity activity;

    public SearchTagResultsAdapter(ArrayList<InstagramSearchTag> tagList, Activity act) {
        searchTagArrayList = tagList;
        activity = act;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_search_result_simple, parent, false);
        return new SearchResultViewHolder(view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PhotoGridActivity.class);
                intent.putExtra("type", "tag");
                intent.putExtra("tagName", (String) v.getTag());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        final InstagramSearchTag instagramTag = searchTagArrayList.get(holder.getLayoutPosition());
        holder.itemView.setTag(instagramTag.tag);
        holder.setAvatar(null);
        holder.setTitle("#" + instagramTag.tag);
        holder.setDescription(String.format("%s post%s", instagramTag.count, instagramTag.count > 1 ? "s" : ""));
    }

    @Override
    public int getItemCount() {
        return searchTagArrayList.size();
    }
}
