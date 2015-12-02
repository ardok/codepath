package com.codepath.instagram.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramSearchTag;
import com.codepath.instagram.models.InstagramUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {
    private static final String TAG = "Utils";
    private static final NumberFormat numberFormat;

    private static final long DAY = 24 * 60 * 60 * 1000;
    private static final long HOUR = 60 * 60 * 1000;
    private static final long MINUTE = 60 * 1000;

    private static final SimpleDateFormat OUT_DAY_FORMAT = new SimpleDateFormat("MMM d, yyyy", Locale.US);
    private static final SimpleDateFormat IN_DAY_FORMAT = new SimpleDateFormat("KK:mm a", Locale.US);
    private static final SimpleDateFormat IN_HOUR_FORMAT = new SimpleDateFormat("KK:mm a", Locale.US);
    private static final SimpleDateFormat IN_MINUTE_FORMAT = new SimpleDateFormat("'Just now'", Locale.US);

    static {
        numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);
    }

    public static String formatNumberForDisplay(int number) {
        return numberFormat.format(number);
    }

    public static JSONObject loadJsonFromAsset(Context context, String fileName) throws IOException, JSONException {
        InputStream inputStream = context.getResources().getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        String line;
        StringBuilder builder = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }

        JSONObject result = new JSONObject(builder.toString());

        inputStream.close();
        bufferedReader.close();

        return result;
    }

    public static List<InstagramPost> decodePostsFromJsonResponse(JSONObject jsonObject) {
        List<InstagramPost> posts = InstagramPost.fromJson(getDataJsonArray(jsonObject));
        return posts == null ? new ArrayList<InstagramPost>() : posts;
    }

    public static List<InstagramComment> decodeCommentsFromJsonResponse(JSONObject jsonObject) {
        List<InstagramComment> comments = InstagramComment.fromJson(getDataJsonArray(jsonObject));
        return comments == null ? new ArrayList<InstagramComment>() : comments;
    }

    public static List<InstagramUser> decodeUsersFromJsonResponse(JSONObject jsonObject) {
        List<InstagramUser> users = InstagramUser.fromJson(getDataJsonArray(jsonObject));
        return users == null ? new ArrayList<InstagramUser>() : users;
    }

    public static List<InstagramSearchTag> decodeSearchTagsFromJsonResponse(JSONObject jsonObject) {
        List<InstagramSearchTag> searchTags = InstagramSearchTag.fromJson(getDataJsonArray(jsonObject));
        return searchTags == null ? new ArrayList<InstagramSearchTag>() : searchTags;
    }

    private static JSONArray getDataJsonArray(JSONObject jsonObject) {
        JSONArray jsonArray = null;
        if (jsonObject != null) {
            jsonArray = jsonObject.optJSONArray("data");
        }
        return jsonArray;
    }

    // TODO use all these helper later
    private static boolean inLastDay(long millisecond) {
        return millisecond > System.currentTimeMillis() - DAY;
    }

    private static boolean inLastHour(long millisecond) {
        return millisecond > System.currentTimeMillis() - HOUR;
    }

    private static boolean inLastMinute(long millisecond) {
        return millisecond > System.currentTimeMillis() - MINUTE;
    }
    // TODO end ---------

    public static String formatDateFromLong(long millisecond) {
        // TODO use this later
//        if (inLastDay(millisecond)) {
//            return IN_DAY_FORMAT.format(new Date(millisecond));
//        }
//        if (inLastHour(millisecond)) {
//            return IN_HOUR_FORMAT.format(new Date(millisecond));
//        }
//        if (inLastMinute(millisecond)) {
//            return IN_MINUTE_FORMAT.format(new Date(millisecond));
//        }

        return OUT_DAY_FORMAT.format(new Date(millisecond));
    }

    public static SpannableStringBuilder getStyledPosterNameAndText(Context context, String posterName, String text) {
        ForegroundColorSpan blueForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.blue_text));
        ForegroundColorSpan grayForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.gray_text));
        TypefaceSpan sansSerifSpan = new TypefaceSpan("sans-serif");
        TypefaceSpan sansSerifMediumSpan = new TypefaceSpan("sans-serif-medium");

        SpannableStringBuilder ssb = new SpannableStringBuilder(posterName);
        ssb.setSpan(
                blueForegroundColorSpan,            // the span to add
                0,                                 // the start of the span (inclusive)
                posterName.length(),                      // the end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssb.setSpan(
                sansSerifSpan,
                0,
                posterName.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        ssb.append(" ");

        if (text != null) {
            ssb.append(text);
            ssb.setSpan(
                    grayForegroundColorSpan,
                    posterName.length() + 2,    // add the empty space into consideration, hence `2`
                    ssb.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            ssb.setSpan(
                    sansSerifMediumSpan,
                    posterName.length() + 2,
                    ssb.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        return ssb;
    }

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
