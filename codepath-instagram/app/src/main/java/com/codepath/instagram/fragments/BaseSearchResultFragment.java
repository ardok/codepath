package com.codepath.instagram.fragments;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.instagram.R;

import butterknife.Bind;

public class BaseSearchResultFragment extends Fragment {
    RecyclerView.LayoutManager searchLayoutManager;

    @Bind(R.id.searchRV) RecyclerView searchRV;
    @Bind(R.id.searchEmptyTV) TextView searchEmptyTV;
    @Bind(R.id.searchProgressBar) ProgressBar searchProgressBar;
    @Bind(R.id.searchRL) RelativeLayout searchRL;

    String query;

    protected void showSearchFailureSnackbar() {
        final Snackbar sbFailed = Snackbar.make(searchRL, "Failed searching", Snackbar.LENGTH_LONG);
        sbFailed.setAction("Try Again", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(query);
                sbFailed.dismiss();
            }
        });
        sbFailed.show();
    }

    public void search(String q) {
        query = q;

        // TODO override me
    }

    public void setQuery(String q) {
        query = q;
    }

    protected void setStateSearching() {
        searchEmptyTV.setVisibility(View.GONE);
        searchProgressBar.setVisibility(View.VISIBLE);
        searchRV.setVisibility(View.GONE);
    }

    protected void setStateSearchingDone(int listSize) {
        searchProgressBar.setVisibility(View.GONE);
        if (listSize == 0) {
            searchEmptyTV.setVisibility(View.VISIBLE);
        } else {
            searchRV.setVisibility(View.VISIBLE);
        }
    }
}
