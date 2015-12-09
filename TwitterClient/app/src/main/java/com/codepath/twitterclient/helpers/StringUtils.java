package com.codepath.twitterclient.helpers;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class StringUtils {
    public static final String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    public static String getRelativeTimeAgo(String rawJsonDate) {
        // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
