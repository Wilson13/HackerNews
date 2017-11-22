package com.wilson.hackernews;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wilson.hackernews.activity.MainActivity;
import com.wilson.hackernews.di.AppComponent;
import com.wilson.hackernews.mvp.GetHackerNewsContract;
import com.wilson.hackernews.mvp.HackerNewsModel;
import com.wilson.hackernews.other.MyApp;
import com.wilson.hackernews.other.RetrofitSingleton;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.wilson.hackernews.other.Constants.HACKER_NEWS_BASE_URL;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(
            MainActivity.class,
            true,
            // false: do not launch the activity immediately
            false);

    @Mock
    private
    HackerNewsModel dataSource;

    private final CountDownLatch latch = new CountDownLatch(1);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        MyApp app = (MyApp) instrumentation.getTargetContext().getApplicationContext();
        AppComponent component = app.getAppComponent();

        // inject dagger
        //component.inject(this);
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.wilson.hackernews", appContext.getPackageName());
    }

    @Test
    public void MyAppTest() throws Exception {
        Assert.assertNotNull(MyApp.get());
    }

    @Test
    public void TopStoriesFragmentIsDisplayedTest() throws InterruptedException {
        Intent intent = new Intent();
        mainActivity.launchActivity(intent);

        // Execute a same api call as the actual app to simulate the delay time
        GetHackerNewsContract.APIModel apiModel = RetrofitSingleton.getClient(HACKER_NEWS_BASE_URL).create(GetHackerNewsContract.APIModel.class);
        HackerNewsModel dataSource = new HackerNewsModel(apiModel);

        // Execute a same api call as the actual app to simulate the delay time
        dataSource.getTopStories().subscribe(hackerNewsStories -> latch.countDown(), error -> {return;});
        latch.await();

        onView(withId(R.id.srl_top_stories)).check(matches(isDisplayed()));
        onView(withId(R.id.ll_stories)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_top_stories)).check(matches(isDisplayed()));
        onView(withId(R.id.ll_empty_stories)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        // Click first story on the list
        //onData(anything()).inAdapterView(withId(R.id.rv_top_stories)).atPosition(0).perform(click());
        onView(withId(R.id.rv_top_stories))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.srl_comments)).check(matches(isDisplayed()));

        //Espresso.pressBack();
        //onView(withId(R.id.srl_top_stories)).check(matches(not(isDisplayed())));
    }
}
