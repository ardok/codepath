package com.codepath.twitterclient.helpers;

import android.widget.ImageView;

import com.codepath.twitterclient.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ImageUtils {
    public static void setPicassoImage(ImageView iv, String url) {
        // Transformation for rounded picture
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(1)
                .oval(false)
                .build();
        Picasso.with(iv.getContext()).load(url)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .transform(transformation)
                .into(iv);
    }
}
