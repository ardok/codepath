package com.codepath.instagram.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.CommentsActivity;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.PostItemViewHolder> {

    private ArrayList<InstagramPost> mInstagramPostList;
    private Activity mActivity;
    private String mClientId;

    public InstagramPostsAdapter(Activity activity,
                                 ArrayList<InstagramPost> instagramPostArrayList,
                                 String clientId) {
        mActivity = activity;
        mInstagramPostList = instagramPostArrayList;
        mClientId = clientId;
        Collections.sort(instagramPostArrayList, new Comparator<InstagramPost>() {
            @Override
            public int compare(InstagramPost lhs, InstagramPost rhs) {
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
    public PostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_post, parent, false);
        return new PostItemViewHolder(view, mActivity);
    }

    @Override
    public void onBindViewHolder(PostItemViewHolder holder, int position) {
        final InstagramPost instagramPost = mInstagramPostList.get(holder.getLayoutPosition());
        holder.clientId = mClientId;
        holder.mediaId = instagramPost.mediaId;
        holder.setPosterName(instagramPost.user.userName);
        holder.setPostDate(instagramPost.createdTime * 1000L);
        holder.setPosterPicture(instagramPost.user.profilePictureUrl);
        holder.setPostImage(instagramPost.image.imageUrl);
        holder.setLikeCount(instagramPost.likesCount);
        holder.setCaptionText(instagramPost.user.userName, instagramPost.caption);
        holder.setViewAllComments(instagramPost.commentsCount);
        holder.setUpComments(instagramPost.comments);
    }

    @Override
    public int getItemCount() {
        return mInstagramPostList.size();
    }

    static class PostItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.profileIV) ImageView posterPictureIV;
        @Bind(R.id.profileTV) TextView posterNameTV;
        @Bind(R.id.postDateTV) TextView postDateTV;
        @Bind(R.id.postIV) ImageView postIV;
        @Bind(R.id.likeCountTV) TextView likeCountTV;
        @Bind(R.id.captionTV) TextView captionTV;
        @Bind(R.id.commentsLL) LinearLayout commentsLL;
        @Bind(R.id.viewAllCommentsTV) TextView viewAllCommentsTV;
        @Bind(R.id.moreIV) ImageView moreIV;

        String clientId;
        String mediaId;

        public PostItemViewHolder(View itemView, final Activity activity) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            viewAllCommentsTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, CommentsActivity.class);
                    intent.putExtra("clientId", clientId);
                    intent.putExtra("mediaId", mediaId);
                    intent.putExtra("code", 100);
                    activity.startActivity(intent);
                }
            });

            moreIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMorePopup(activity);
                }
            });
        }

        public void setViewAllComments(int commentCount) {
            viewAllCommentsTV.setText(String.format("View all %s %s", commentCount, commentCount > 1 ? "comments" : "comment"));
        }

        public void setPosterName(String name) {
            posterNameTV.setText(name);
        }

        public void setCaptionText(String posterName, String captionText) {
            if (captionText == null || captionText.isEmpty()) {
                captionTV.setVisibility(View.GONE);
                return;
            }
            SpannableStringBuilder ssb = Utils.getStyledPosterNameAndText(captionTV.getContext(), posterName, captionText);
            captionTV.setText(ssb, TextView.BufferType.EDITABLE);
        }

        public void setPostDate(long milliseconds) {
            postDateTV.setText(Utils.formatDateFromLong(milliseconds));
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

        public void setUpComments(List<InstagramComment> commentList) {
            if (commentList == null || commentList.size() == 0) {
                return;
            }

            commentsLL.removeAllViews();
            // We only wanna show the last 2 comments
            // We could also use the commentsCount from InstagramPost object here
            int commentsSize = commentList.size();

            if (commentsSize < 2) {
                viewAllCommentsTV.setVisibility(View.GONE);
            }

            for (int start = commentsSize - 1, loopCount = 0; loopCount < 2 && start > -1; start--, loopCount++) {
                InstagramComment instagramComment = commentList.get(start);
                View commentView = LayoutInflater.from(commentsLL.getContext()).inflate(
                        R.layout.layout_item_text_comment,
                        commentsLL,
                        false
                );
                SpannableStringBuilder ssb = Utils.getStyledPosterNameAndText(commentView.getContext(), instagramComment.user.userName, instagramComment.text);
                ((TextView) commentView.findViewById(R.id.commentTV)).setText(ssb, TextView.BufferType.EDITABLE);
                commentsLL.addView(commentView);
            }
        }

        private void showMorePopup(final Activity activity) {
            PopupMenu popup = new PopupMenu(moreIV.getContext(), moreIV);
            // Inflate the menu from xml
            popup.getMenuInflater().inflate(R.menu.menu_popup_post, popup.getMenu());
            // Setup menu item selection
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_share:
                            onShareItem(activity, postIV);
                            return true;
                        default:
                            return false;
                    }
                }
            });
            // Handle dismissal with: popup.setOnDismissListener(...);
            // Show the menu
            popup.show();
        }

        // Can be triggered by a view event such as a button press
        public void onShareItem(Activity activity, ImageView ivImage) {
            // Get access to bitmap image from view
            // Get access to the URI for the bitmap
            Uri bmpUri = getLocalBitmapUri(ivImage);
            if (bmpUri != null) {
                // Construct a ShareIntent with link to image
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/*");
                // Launch sharing dialog for image
                activity.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                return;
            }

            Toast.makeText(ivImage.getContext(), "Failed sharing image", Toast.LENGTH_SHORT).show();
        }

        // Returns the URI path to the Bitmap displayed in specified ImageView
        public Uri getLocalBitmapUri(ImageView imageView) {
            // Extract Bitmap from ImageView drawable
            Drawable drawable = imageView.getDrawable();
            Bitmap bmp;
            if (drawable instanceof BitmapDrawable){
                bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            } else {
                return null;
            }
            // Store image to default external storage directory
            Uri bmpUri = null;
            try {
                File file =  new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
                file.getParentFile().mkdirs();
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                bmpUri = Uri.fromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmpUri;
        }
    }
}
