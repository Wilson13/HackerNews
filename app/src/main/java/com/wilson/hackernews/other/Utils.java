package com.wilson.hackernews.other;

public class Utils {

    private static final int NUM_SECONDS_HOUR = 60 * 60;
    private static final int NUM_SECONDS_MINUTE = 60;

    public static String getElapsedTime(long timeCommented, long currentTime) {

        String elapsedTime = "";
        long commentedTime = currentTime - timeCommented;

        // If elapsedTime is more than an hour
        if (commentedTime > NUM_SECONDS_HOUR) {

            long commentTimeHour = commentedTime / NUM_SECONDS_HOUR;
            elapsedTime = commentTimeHour + " hour";

            if (commentTimeHour > 1)
                elapsedTime = commentTimeHour + " hours";
        }
        // If elapsedTime is more than a minute
        else if (commentedTime > NUM_SECONDS_MINUTE) {

            long commentTimeMin = commentedTime / NUM_SECONDS_MINUTE;
            elapsedTime = commentedTime + " min";

            if (commentTimeMin > 1)
                elapsedTime = commentTimeMin + " mins";
        }
        // If elapsedTime is more than a second
        else if (commentedTime > (1)) {
            elapsedTime = commentedTime + " secs";
        }
        // If elapsedTime is more less than a second (just now)
        else {
            elapsedTime = 0 + " sec";
        }
        return elapsedTime;
    }

}
