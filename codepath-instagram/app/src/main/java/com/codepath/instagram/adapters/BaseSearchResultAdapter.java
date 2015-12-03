package com.codepath.instagram.adapters;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.core.MainApplication;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class BaseSearchResultAdapter extends RecyclerView.Adapter<BaseSearchResultAdapter.SearchResultViewHolder> {
    static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.searchAvatarIV) ImageView searchAvatarIV;
        @Bind(R.id.searchTitleTV) TextView searchTitleTV;
        @Bind(R.id.searchDescriptionTV) TextView searchDescriptionTV;

        public SearchResultViewHolder(final View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(onClickListener);
        }

        public void setAvatar(String url) {
            if (url == null) {
                searchAvatarIV.setVisibility(View.GONE);
                return;
            }
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(30)
                    .oval(false)
                    .build();

            Picasso.with(searchAvatarIV.getContext()).load(url)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .transform(transformation)
                    .into(searchAvatarIV);
        }

        public void setTitle(String title) {
            if (searchAvatarIV.getVisibility() == View.GONE) {
                RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                rlp.addRule(RelativeLayout.CENTER_VERTICAL);
                Resources r = MainApplication.sharedApplication().getResources();
                int px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        28,
                        r.getDisplayMetrics()
                );
                rlp.setMargins(px, 0, 0, 0);
                searchTitleTV.setLayoutParams(rlp);
            }
            searchTitleTV.setText(title);
        }

        public void setDescription(String description) {
            if (description != null) {
                searchDescriptionTV.setText(description);
            }
        }
    }
}
