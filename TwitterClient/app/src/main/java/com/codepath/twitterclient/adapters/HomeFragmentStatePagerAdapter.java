package com.codepath.twitterclient.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.codepath.twitterclient.fragments.HomeTimelineFragment;
import com.codepath.twitterclient.fragments.MentionsFragment;

public class HomeFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {

    private static final int NUM_ITEMS = 2;

    private static final int[] imageResId = {
            android.R.mipmap.sym_def_app_icon,
            android.R.mipmap.sym_def_app_icon
    };

    private static final String[] titles = {
            "Home",
            "Mentions"
    };

    HomeTimelineFragment homeTimelineFragment;
    MentionsFragment mentionsFragment;

    Activity activity;

    public HomeFragmentStatePagerAdapter(FragmentManager fragmentManager, Activity act) {
        super(fragmentManager);
        activity = act;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (homeTimelineFragment == null) {
                    homeTimelineFragment = HomeTimelineFragment.newInstance(null);
                }
                return homeTimelineFragment;
            case 1:
                if (mentionsFragment == null) {
                    mentionsFragment = MentionsFragment.newInstance(null);
                }
                return mentionsFragment;
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(activity, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString ss = new SpannableString("  " + titles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        ss.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
