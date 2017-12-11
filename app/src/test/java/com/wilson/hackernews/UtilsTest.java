package com.wilson.hackernews;

import android.os.Build;
import android.text.Spanned;
import android.util.Log;

import com.wilson.hackernews.other.Utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


/**
 * This test is set up to run with Robolectric to test (emulate) device
 * with Android version of 14 (lower than 24) for a increased code coverage.
 *
 * Note: Robolectric support only from API 16 onwards (>=16).
  */

@RunWith(RobolectricTestRunner.class)
public class UtilsTest {

    @Config(sdk= Build.VERSION_CODES.JELLY_BEAN)
    @Test
    public void UtilsTest() {
        Log.d("Utils: ", String.valueOf(Build.VERSION.SDK_INT));
        System.out.print(String.valueOf(Build.VERSION.SDK_INT));
        String testContent = "Same story as always: you take the risk, invest your time and break up your life. If it works you are welcome to stay. If it doesn&#x27;t work you get cut loose and you likely will take years to recover whereas the other side hasn&#x27;t risked a thing.<p>These kind of propositions only make sense if you have nothing to lose.";
        Spanned styleText = Utils.getStyledText(testContent);
        Assert.assertNotNull(styleText);
    }
}
