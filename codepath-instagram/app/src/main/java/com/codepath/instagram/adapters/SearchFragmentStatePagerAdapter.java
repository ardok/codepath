package com.codepath.instagram.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.instagram.fragments.SearchTagsResultFragment;
import com.codepath.instagram.fragments.SearchUsersResultFragment;
import com.codepath.instagram.helpers.SmartFragmentStatePagerAdapter;

public class SearchFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {
    private static final int NUM_ITEMS = 2;
    private static final String[] PAGE_TITLE = {
        "Users",
        "Tags"
    };

    SearchTagsResultFragment searchTagsResultFragment;
    SearchUsersResultFragment searchUsersResultFragment;

    public SearchFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
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
                if (searchUsersResultFragment == null) {
                    searchUsersResultFragment = SearchUsersResultFragment.newInstance(null);
                }
                return searchUsersResultFragment;
            case 1:
                if (searchTagsResultFragment == null) {
                    searchTagsResultFragment = SearchTagsResultFragment.newInstance(null);
                }
                return searchTagsResultFragment;
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_TITLE[position];
    }
}
