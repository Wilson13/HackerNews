package com.wilson.hackernews.other;

import static com.wilson.hackernews.other.Constants.NUM_SECONDS_HOUR;
import static com.wilson.hackernews.other.Constants.NUM_SECONDS_MINUTE;

public class Utils {

    public static String getElapsedTime(long timeCommented, long currentTime) {

        String elapsedTime = "";
        long commentedTime = currentTime - timeCommented;
        long commentTimeHour;
        long commentTimeMin;

        // If elapsedTime is more than an hour
        if (commentedTime >= NUM_SECONDS_HOUR) {
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

}
