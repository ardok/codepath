package com.codepath.instagram.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.PhotoGridAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotoGridFragment extends BaseSwipeFragment {
    RecyclerView.LayoutManager ipLayoutManager;

    @Bind(R.id.photoGridRV) RecyclerView photoGridRV;

    PhotoGridAdapter photoGridAdapter;

    ArrayList<InstagramPost> ipList;

    String tagName;
    String userId;
    String type;

    public static PhotoGridFragment newInstance(String type, String tagName, String userId) {
        PhotoGridFragment fragment = new PhotoGridFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tagName", tagName);
        bundle.putString("userId", userId);
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_photo_grid, container, false);
        // Setup handles to view objects here
        // etFoo = (EditText) view.findViewById(R.id.etFoo);

        ButterKnife.bind(this, view);

        setUpRecyclerView();
        setUpSwipeToRefresh(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        tagName = args.getString("tagName");
        userId = args.getString("userId");
        type = args.getString("type");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMedia();
    }


    private void getMedia() {
        Context c = getContext();
        if (!Utils.isNetworkAvailable(c)) {
            Snackbar.make(photoGridRV, "Please make sure you have network connection", Snackbar.LENGTH_LONG).show();
            swipeContainer.setRefreshing(false);
            return;
        }

        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                swipeContainer.setRefreshing(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject meta = response.getJSONObject("meta");
                    if (meta.getInt("code") != 200) {
                        showFetchFeedFailureSnackbar();
                        return;
                    }
                    ipList.clear(); // clear existing items if needed
                    ipList.addAll(Utils.decodePostsFromJsonResponse(response));
                    photoGridAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    showFetchFeedFailureSnackbar();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
                showFetchFeedFailureSnackbar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                swipeContainer.setRefreshing(false);
                showFetchFeedFailureSnackbar();
            }
        };

        if (type != null) {
            if (type.equals("tag")) {
                MainApplication.getRestClient().getTagMediaRecent(tagName, responseHandler);
            } else if (type.equals("user")) {
                MainApplication.getRestClient().getUserMediaRecent(userId, responseHandler);
            }
        }
    }

    private void setUpRecyclerView() {
        ipList = new ArrayList<>();
        photoGridAdapter = new PhotoGridAdapter(getActivity(), ipList);
        ipLayoutManager = new GridLayoutManager(getContext(), 3);
        photoGridRV.setHasFixedSize(true);
        photoGridRV.setLayoutManager(ipLayoutManager);
        photoGridRV.setAdapter(photoGridAdapter);
    }

    @Override
    protected void onSwipe() {
        getMedia();
    }

    private void showFetchFeedFailureSnackbar() {
        final Snackbar sbFailed = Snackbar.make(photoGridRV, "Failed fetching photos", Snackbar.LENGTH_LONG);
        sbFailed.setAction("Refresh", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMedia();
                sbFailed.dismiss();
            }
        });
        sbFailed.show();
    }
}
