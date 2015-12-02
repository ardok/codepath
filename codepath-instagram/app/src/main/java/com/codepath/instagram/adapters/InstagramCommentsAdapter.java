package com.codepath.instagram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InstagramCommentsAdapter extends RecyclerView.Adapter<InstagramCommentsAdapter.CommentItemViewHolder> {
    private ArrayList<InstagramComment> mInstagramCommentList;

    public InstagramCommentsAdapter(ArrayList<InstagramComment> instagramCommentList) {
        // Always sort by newest
        mInstagramCommentList = instagramCommentList;
        Collections.sort(instagramCommentList, new Comparator<InstagramComment>() {
            @Override
            public int compare(InstagramComment lhs, InstagramComment rhs) {
                if (lhs.createdTime > rhs.createdTime) {
                    return 1;
                }
                if (lhs.createdTime < rhs.createdTime) {
                    return -1;
                }
                return 0;
            }
        });
    }

    @Override
    public CommentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_comment, parent, false);
        return new CommentItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentItemViewHolder holder, int position) {
        final InstagramComment instagramComment = mInstagramCommentList.get(holder.getLayoutPosition());
        holder.setCommentAvatar(instagramComment.user.profilePictureUrl);
        holder.setCommentText(instagramComment.user.userName, instagramComment.text);
        holder.setCommentDate(instagramComment.createdTime * 1000L);
    }

    @Override
    public int getItemCount() {
        return mInstagramCommentList.size();
    }

    static class CommentItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.commentAvatarIV) ImageView commentAvatarIV;
        @Bind(R.id.commentTV) TextView commentTV;
        @Bind(R.id.commentDate) TextView commentDateTV;
        
        public CommentItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            Context context = itemView.getContext();
        }
        
        public void setCommentAvatar(String url) {
            // Transformation for rounded picture
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderWidthDp(3)
                    .cornerRadiusDp(30)
                    .oval(false)
                    .build();

            Picasso.with(commentAvatarIV.getContext())
                    .load(url)
                    .fit()
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .transform(transformation)
                    .into(commentAvatarIV);
        }
        
        public void setCommentText(String posterName, String commentText) {
            if (commentText == null || commentText.isEmpty()) {
                commentTV.setVisibility(View.GONE);
                return;
            }
            SpannableStringBuilder ssb = Utils.getStyledPosterNameAndText(commentTV.getContext(), posterName, commentText);
            commentTV.setText(ssb, TextView.BufferType.EDITABLE);
        }
        
        public void setCommentDate(long milliseconds) {
            commentDateTV.setText(Utils.formatDateFromLong(milliseconds));
        }
    }

}
