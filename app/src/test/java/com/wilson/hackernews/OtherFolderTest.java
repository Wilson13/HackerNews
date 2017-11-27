package com.wilson.hackernews;

import com.wilson.hackernews.other.Constants;
import com.wilson.hackernews.other.MyApp;
import com.wilson.hackernews.other.RetrofitSingleton;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import retrofit2.Retrofit;

import static com.wilson.hackernews.other.Constants.HACKER_NEWS_BASE_URL;
import static com.wilson.hackernews.other.Utils.getCorrectURL;
import static com.wilson.hackernews.other.Utils.getElapsedTime;
import static org.hamcrest.CoreMatchers.instanceOf;

@RunWith(MockitoJUnitRunner.class)
public class OtherFolderTest {


    @Mock
    MyApp myApp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void RetrofitSingletonTest() {
        RetrofitSingleton.getClient(HACKER_NEWS_BASE_URL);
        Assert.assertThat(new RetrofitSingleton().getClient(HACKER_NEWS_BASE_URL), instanceOf(Retrofit.class));
    }

    @Test
    public void UtilsGetElapsedTimeTest() {
        long currentTime = System.currentTimeMillis() / 1000;
        long oneHourAgo = currentTime - Constants.NUM_SECONDS_HOUR;
        long twoHourAgo = currentTime - (2 * Constants.NUM_SECONDS_HOUR);
        long oneMinAgo = currentTime - Constants.NUM_SECONDS_MINUTE;
        long twoMinsAgo = currentTime - (2 * Constants.NUM_SECONDS_MINUTE);
        long zeroSecAgo = currentTime;
        long twoSescAgo = currentTime - 2;

        // Test 1 hour
        Assert.assertEquals("1 hour", getElapsedTime(oneHourAgo, currentTime));

        // Test 2 hours
        Assert.assertEquals("2 hours", getElapsedTime(twoHourAgo, currentTime));

        // Test 1 minute
        Assert.assertEquals("1 min", getElapsedTime(oneMinAgo, currentTime));

        // Test 2 minutes
        Assert.assertEquals("2 mins", getElapsedTime(twoMinsAgo, currentTime));

        // Test 2 minutes
        Assert.assertEquals("2 mins", getElapsedTime(twoMinsAgo, currentTime));

        // Test 2 minutes
        Assert.assertEquals("1 sec", getElapsedTime(zeroSecAgo, currentTime));

        // Test 2 minutes
        Assert.assertEquals("2 secs", getElapsedTime(twoSescAgo, currentTime));
    }

    @Test
    public void UtilsGetCorrectURL() {
        String correctURL = "http://google.com";
        String url = "google.com";
        Assert.assertEquals(correctURL, getCorrectURL(url));
    }
}
