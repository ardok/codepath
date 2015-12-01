package com.codepath.instagram.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.PostItemViewHolder> {

    private ArrayList<InstagramPost> mInstagramPostList;

    public InstagramPostsAdapter(ArrayList<InstagramPost> instagramPostArrayList) {
        mInstagramPostList = instagramPostArrayList;
    }

    @Override
    public PostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_post, parent, false);
        return new PostItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostItemViewHolder holder, int position) {
        final InstagramPost instagramPost = mInstagramPostList.get(holder.getLayoutPosition());
        holder.setPosterName(instagramPost.user.userName);
        holder.setPostDate(Utils.formatDateFromLong(instagramPost.createdTime * 1000L));
        holder.setPosterPicture(instagramPost.user.profilePictureUrl);
        holder.setPostImage(instagramPost.image.imageUrl);
        holder.setLikeCount(instagramPost.likesCount);
        holder.setCaptionText(instagramPost.user.userName, instagramPost.caption);
    }

    @Override
    public int getItemCount() {
        return mInstagramPostList.size();
    }

    static class PostItemViewHolder extends RecyclerView.ViewHolder {
        ImageView posterPictureIV;
        TextView posterNameTV;
        TextView postDateTV;
        ImageView postIV;
        TextView likeCountTV;
        TextView captionTV;

        ForegroundColorSpan blueForegroundColorSpan;
        ForegroundColorSpan grayForegroundColorSpan;
        TypefaceSpan sansSerifSpan;
        TypefaceSpan sansSerifMediumSpan;

        public PostItemViewHolder(View itemView) {
            super(itemView);
            posterPictureIV = (ImageView) itemView.findViewById(R.id.profileIV);
            posterNameTV = (TextView) itemView.findViewById(R.id.profileTV);
            postDateTV = (TextView) itemView.findViewById(R.id.postDateTV);
            postIV = (ImageView) itemView.findViewById(R.id.postIV);
            likeCountTV = (TextView) itemView.findViewById(R.id.likeCountTV);
            captionTV = (TextView) itemView.findViewById(R.id.captionTV);

            Context context = itemView.getContext();
            blueForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.blue_text));
            grayForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.gray_text));
            sansSerifSpan = new TypefaceSpan("sans-serif");
            sansSerifMediumSpan = new TypefaceSpan("sans-serif-medium");
        }

        public void setPosterName(String name) {
            posterNameTV.setText(name);
        }

        public void setCaptionText(String posterName, String captionText) {
            SpannableStringBuilder ssb = new SpannableStringBuilder(posterName);
            ssb.setSpan(
                    blueForegroundColorSpan,            // the span to add
                    0,                                 // the start of the span (inclusive)
                    posterName.length(),                      // the end of the span (exclusive)
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            ssb.setSpan(
                    sansSerifSpan,
                    0,
                    posterName.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            ssb.append(" ");

            if (captionText != null) {
                ssb.append(captionText);
                ssb.setSpan(
                        grayForegroundColorSpan,
                        posterName.length() + 2,    // add the empty space into consideration, hence `2`
                        ssb.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                ssb.setSpan(
                        sansSerifMediumSpan,
                        posterName.length() + 2,
                        ssb.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }

            captionTV.setText(ssb, TextView.BufferType.EDITABLE);
        }

        public void setPostDate(String dateText) {
            postDateTV.setText(dateText);
        }

        public void setPosterPicture(String url) {
            // Transformation for rounded picture
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderWidthDp(3)
                    .cornerRadiusDp(30)
                    .oval(false)
                    .build();

            Picasso.with(postIV.getContext()).load(url)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .transform(transformation)
                    .into(posterPictureIV);
        }

        public void setPostImage(String url) {
            Picasso.with(postIV.getContext()).load(url)
                    .fit()
                    .placeholder(R.drawable.gray_oval)
                    .error(R.drawable.gray_rectangle)
                    .into(postIV);
        }

        public void setLikeCount(int count) {
            String str = Utils.formatNumberForDisplay(count) + " like" + (count > 1 ? "s" : "");
            likeCountTV.setText(str);
        }
    }
}
