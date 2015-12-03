package com.codepath.instagram.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramImage;
import com.codepath.instagram.models.InstagramPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.PhotoGridViewHolder> {

    private ArrayList<InstagramPost> mInstagramPostList;
    private Activity mActivity;

    public PhotoGridAdapter(Activity activity,
                            ArrayList<InstagramPost> instagramPostArrayList) {
        mActivity = activity;
        mInstagramPostList = instagramPostArrayList;
    }

    @Override
    public PhotoGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_photo, parent, false);
        return new PhotoGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoGridViewHolder holder, int position) {
        final InstagramPost instagramPost = mInstagramPostList.get(holder.getLayoutPosition());
        InstagramImage img = instagramPost.image;
        if (img != null) {
            holder.setPhoto(img.imageUrl);
        }
    }

    @Override
    public int getItemCount() {
        return mInstagramPostList.size();
    }

    static class PhotoGridViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.photoIV) ImageView photoIV;

        public PhotoGridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setPhoto(String url) {
            Picasso.with(photoIV.getContext())
                    .load(url)
                    .fit()
                    .placeholder(R.drawable.gray_rectangle)
                    .error(R.drawable.gray_rectangle)
                    .into(photoIV);
        }
    }
}
