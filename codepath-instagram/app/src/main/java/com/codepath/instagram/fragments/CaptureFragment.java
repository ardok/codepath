package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class CaptureFragment extends Fragment {
    public static CaptureFragment newInstance(Bundle args) {
        CaptureFragment fragment = new CaptureFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }
}
