package com.wilson.hackernews;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wilson.hackernews.activity.MainActivity;
import com.wilson.hackernews.di.AppComponent;
import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.mvp.HNStoriesPresenter;
import com.wilson.hackernews.mvp.HackerNewsAPI;
import com.wilson.hackernews.other.MyApp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PresenterInstrumentedTest {

    @Inject
    private StoriesViewImpl storiesView;
    @Inject
    private HackerNewsAPI apiService;

    HNStoriesPresenter presenter;

    private String[] topStoriesID;
    private String storyID;
    private String commentID;

    HackerNewsStory story;
    HackerNewsStory story2;
    HackerNewsComment comment;
    ArrayList hackerNewsStoryList;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(
            MainActivity.class,
            true,
            // false: do not launch the activity immediately
            false);

    @Before
    public void setup() {
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
    public void PresenterTest() throws Exception {
        //HNStoriesPresenter presenter = new HNStoriesPresenter(apiService);
        //presenter.setView(storiesView);
    }
}
