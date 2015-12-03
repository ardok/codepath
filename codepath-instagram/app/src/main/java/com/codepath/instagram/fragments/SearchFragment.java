package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.SearchFragmentStatePagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment {
    @Bind(R.id.searchVP) ViewPager searchVP;
    @Bind(R.id.searchTL) TabLayout searchTL;

    SearchFragmentStatePagerAdapter searchFragmentStatePagerAdapter;

    String query;

    public static SearchFragment newInstance(Bundle args) {
        SearchFragment fragment = new SearchFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        // Setup handles to view objects here
        // etFoo = (EditText) view.findViewById(R.id.etFoo);

        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        searchFragmentStatePagerAdapter = new SearchFragmentStatePagerAdapter(getFragmentManager());
        searchVP.setAdapter(searchFragmentStatePagerAdapter);
        // Give the TabLayout the ViewPager
        searchTL.setupWithViewPager(searchVP);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem item =  menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                query = q;

                // TODO load lazily for each fragment, but I'm not sure how?
                // Let's just send request for both for now
                int pagerCount = searchFragmentStatePagerAdapter.getCount();
                for (int i = 0; i < pagerCount; i++) {
                    BaseSearchResultFragment bsrf = (BaseSearchResultFragment) searchFragmentStatePagerAdapter.getRegisteredFragment(i);
                    if (bsrf != null) {
                        bsrf.search(q);
                    }
                }

                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                item.collapseActionView();

                // Set activity title to search query
                getActivity().setTitle(q);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

}
