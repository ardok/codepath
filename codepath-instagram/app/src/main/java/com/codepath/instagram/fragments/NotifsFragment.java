package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class NotifsFragment extends Fragment {
    public static NotifsFragment newInstance(Bundle args) {
        NotifsFragment fragment = new NotifsFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }
}
