package com.codepath.android.lollipopexercise.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImageUtils {
    public static Target getTargetForVibrantColorInto(final ImageView iv, final View into) {
        return new Target() {
            // Fires when Picasso finishes loading the bitmap for the target
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                iv.setImageBitmap(bitmap);
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch swatch = palette.getVibrantSwatch();
                        if (swatch != null) {
                            into.setBackgroundColor(palette.getVibrantSwatch().getRgb());
                        }
                    }
                });
            }

            // Fires if bitmap fails to load
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }
}
