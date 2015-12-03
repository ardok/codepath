package com.codepath.instagram.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.CaptureFragment;
import com.codepath.instagram.fragments.NotifsFragment;
import com.codepath.instagram.fragments.PostsFragment;
import com.codepath.instagram.fragments.ProfileFragment;
import com.codepath.instagram.fragments.SearchFragment;
import com.codepath.instagram.helpers.SmartFragmentStatePagerAdapter;

public class HomeFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {

    private static final int NUM_ITEMS = 5;

    private static final int[] imageResId = {
            R.drawable.ic_home,
            R.drawable.ic_search,
            R.drawable.ic_capture,
            R.drawable.ic_notifs,
            R.drawable.ic_profile
    };

    PostsFragment postsFragment;
    SearchFragment searchFragment;
    CaptureFragment captureFragment;
    NotifsFragment notifsFragment;
    ProfileFragment profileFragment;

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
                if (postsFragment == null) {
                    postsFragment = PostsFragment.newInstance(null);
                }
                return postsFragment;
            case 1:
                if (searchFragment == null) {
                    searchFragment = SearchFragment.newInstance(null);
                }
                return searchFragment;
            case 2:
                if (captureFragment == null) {
                    captureFragment = CaptureFragment.newInstance(null);
                }
                return captureFragment;
            case 3:
                if (notifsFragment == null) {
                    notifsFragment = NotifsFragment.newInstance(null);
                }
                return notifsFragment;
            case 4:
                if (profileFragment == null) {
                    profileFragment = ProfileFragment.newInstance(null);
                }
                return profileFragment;
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(activity, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
