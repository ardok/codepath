package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ProfileFragment extends Fragment {
    public static ProfileFragment newInstance(Bundle args) {
        ProfileFragment fragment = new ProfileFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }
}
