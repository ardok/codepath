package com.codepath.instagram.models;

import java.io.Serializable;
import java.util.ArrayList;

public class InstagramPostsWrapper implements Serializable {
    private ArrayList<InstagramPost> instagramPostList;

    public InstagramPostsWrapper(ArrayList<InstagramPost> ipList) {
        instagramPostList = ipList;
    }

    public ArrayList<InstagramPost> getInstagramPostList() {
        return instagramPostList;
    }
}
