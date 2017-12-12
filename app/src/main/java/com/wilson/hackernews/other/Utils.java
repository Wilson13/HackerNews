package com.wilson.hackernews.other;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import static com.wilson.hackernews.other.Constants.NUM_SECONDS_DAY;
import static com.wilson.hackernews.other.Constants.NUM_SECONDS_HOUR;
import static com.wilson.hackernews.other.Constants.NUM_SECONDS_MINUTE;

public class Utils {

    public static String getElapsedTime(long timeCommented, long currentTime) {

        String elapsedTime;
        long commentedTime = currentTime - timeCommented;
        long commentTimeHour;
        long commentTimeMin;

        // If elapsedTime is more than a day
        if (commentedTime >= NUM_SECONDS_DAY) {
            commentTimeHour = commentedTime / NUM_SECONDS_DAY;
            elapsedTime = commentTimeHour > 1 ? commentTimeHour + " days" : commentTimeHour + " day";
        }
        // If elapsedTime is more than an hour
        else if (commentedTime >= NUM_SECONDS_HOUR) {
            commentTimeHour = commentedTime / NUM_SECONDS_HOUR;
            elapsedTime = commentTimeHour > 1 ? commentTimeHour + " hours" : commentTimeHour + " hour";
        }
        // If elapsedTime is more than a minute
        else if (commentedTime >= NUM_SECONDS_MINUTE) {
            commentTimeMin = commentedTime / NUM_SECONDS_MINUTE;
            elapsedTime = commentTimeMin > 1 ? commentTimeMin + " mins" : commentTimeMin + " min";
        }
        // If elapsedTime is in seconds
        else {
            elapsedTime = commentedTime > 1 ? commentedTime + " secs" : "1 sec";
        }
        return elapsedTime;
    }

    public static String getCorrectURL(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            return "http://" + url;
        else
            return url;
    }

    public static String getComments(int numComments) {
        if (numComments > 1)
            return numComments + " comments";
        else
            return numComments + " comment";
    }

    public static Spanned getStyledText(String content) {
        // Use a variable to avoid deprecation warning.
        // Could have used a @SuppressWarnings("deprecation") annotation too.
        Spanned result;
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            result = Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY);
        else
            result = Html.fromHtml(content);
        return result;
    }

}
