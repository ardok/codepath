package com.codepath.instagram.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.SearchTagResultsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramSearchTag;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class SearchTagsResultFragment extends BaseSearchResultFragment {
    SearchTagResultsAdapter searchAdapter;

    ArrayList<InstagramSearchTag> istList;

    public static SearchTagsResultFragment newInstance(Bundle args) {
        SearchTagsResultFragment fragment = new SearchTagsResultFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        // Setup handles to view objects here
        // etFoo = (EditText) view.findViewById(R.id.etFoo);

        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);
        setUpRecyclerView();

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
    public void search(String q) {
        super.search(q);
        MainApplication.getRestClient().searchTag(q, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                setStateSearching();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject meta = response.getJSONObject("meta");
                    if (meta.getInt("code") != 200) {
                        showSearchFailureSnackbar();
                        return;
                    }
                    istList.clear(); // clear existing items if needed
                    istList.addAll(Utils.decodeSearchTagsFromJsonResponse(response));
                    searchAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    showSearchFailureSnackbar();
                    e.printStackTrace();
                }
                setStateSearchingDone(istList.size());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                setStateSearchingDone(istList.size());
                showSearchFailureSnackbar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                setStateSearchingDone(istList.size());
                showSearchFailureSnackbar();
            }
        });
    }

    private void setUpRecyclerView() {
        istList = new ArrayList<>();
        searchAdapter = new SearchTagResultsAdapter(istList, getActivity());
        searchLayoutManager = new LinearLayoutManager(getContext());
        searchRV.setLayoutManager(searchLayoutManager);
        searchRV.setAdapter(searchAdapter);
    }
}
