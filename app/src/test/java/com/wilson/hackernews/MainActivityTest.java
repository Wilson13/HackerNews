package com.wilson.hackernews;

import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.wilson.hackernews.activity.MainActivity;
import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.mvp.HackerNewsModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

//@RunWith(AndroidJUnit4.class)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Mock
    HackerNewsModel dataSource;

    private String storyID;
    private String storyID2;
    private String[] topStoriesID;
    private List<String> storiesToPullList = new ArrayList<>();
    private ArrayList hackerNewsStoryList;

    private HackerNewsStory story;
    private HackerNewsStory story2;

    static int storiesCount = 0;
    static int storyPos = 0;
    static boolean commentClicked = false;

    static int commentsCount = 0;
    static int commentPos = 0;
    static boolean replyClicked = false;

    private MainActivity mainActivity;
//    @Rule
//    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(
//            MainActivity.class,
//            true,
//            // false: do not launch the activity immediately
//            false);

    private final CountDownLatch latch = new CountDownLatch(1);

    @Before
    public void setup() {
        //mainActivity = Robolectric.setupActivity(MainActivity.class);
        ActivityController<MainActivity> activityController = Robolectric.buildActivity(MainActivity.class);

        // get the activity instance
        mainActivity = activityController.get();

        initMocks(this);

        // now setup your activity after mock injection
        activityController.setup();

        Scheduler immediate = new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);

        storyID = "15683275";
        storyID2 = "15701417";
        topStoriesID = new String[] {storyID, storyID2};
        storiesToPullList = Arrays.asList(topStoriesID);

        story = new Gson().fromJson("{ \"by\" : \"heshamg\", \"descendants\" : 59, \"id\" : 15683275, \"kids\" : [ 15684947, 15684534, 15684418, 15683718, 15683523, 15683499, 15684849, 15683566, 15683461, 15683463, 15683579, 15684300, 15683851, 15684266, 15683674 ], \"score\" : 141, \"time\" : 1510531519, \"title\" : \"Uber confirms SoftBank has agreed to invest\", \"type\" : \"story\", \"url\" : \"https://techcrunch.com/2017/11/12/uber-confirms-softbank-has-agreed-to-invest-billions-in-uber/\"}", HackerNewsStory.class);
        story2 = new Gson().fromJson("{ \"by\" : \"aeontech\", \"descendants\" : 32, \"id\" : 15701417, \"kids\" : [ 15702188, 15702224, 15701593, 15701728, 15701764, 15701658, 15702192 ], \"score\" : 55, \"time\" : 1510719453, \"title\" : \"WTF is a Source Map\", \"type\" : \"story\", \"url\" : \"https://www.schneems.com/2017/11/14/wtf-is-a-source-map/\"}", HackerNewsStory.class);
        hackerNewsStoryList = new ArrayList<>(Arrays.asList(story, story2));

        // When API getTopStories() is called, return the topStoriesID array defined.
        when(dataSource.getTopStories()).thenReturn(Single.fromObservable(Observable.just(topStoriesID)));

        // When API getTopStories() is called, return the hackerNewsStoryList defined.
        when(dataSource.getStories(storiesToPullList)).thenReturn(Observable.fromIterable(storiesToPullList).flatMap(id -> dataSource.getStory(id)).toList());

        //Single.fromObservable(Observable.fromIterable(hackerNewsStoryList)));

        when(dataSource.getStory(storyID)).thenReturn(Observable.just(story));
        when(dataSource.getStory(storyID2)).thenReturn(Observable.just(story2));
    }

    @Test
    public void shouldNotBeNull() throws InterruptedException {
        assertNotNull(mainActivity);
        FrameLayout frameLayout = (FrameLayout) mainActivity.findViewById(R.id.fl_main);
        assertViewIsVisible(frameLayout);
    }

    private void assertViewIsVisible(View view) {
        assertNotNull(view);
        assertThat(view.getVisibility(), equalTo(View.VISIBLE));
    }
    // Execute a same api call as the actual app to simulate the delay time
        //dataSource.getTopStories().subscribe(hackerNewsStories -> latch.countDown(), error -> {});
        //latch.await();

        // Check that TopStoriesFragment is displayed

//        onView(withId(R.id.ll_stories)).check(matches(isDisplayed()));
//
//        // Check that empty stories layout is not displayed
//        onView(withId(R.id.ll_empty_stories)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
//
//        // Get number of story items
//        onView(withId(R.id.rv_top_stories)).check(new RecyclerViewItemCount());
//
//        // Click first story's comments on the list (might need to change
//        // position if first story has no comments, hence the storyPOS variable).
//        while (!commentClicked && storyPos < storiesCount) {
//            onView(withId(R.id.rv_top_stories))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(storyPos, ClickAction.clickChildViewWithId(R.id.tv_comments)));
//        }
//
//        // Check that CommentsFragment is displayed if "Comments" was clicked
//        if (commentClicked)
//            onView(withId(R.id.ll_comments)).check(matches(isDisplayed()));
//
//        // Get number of comment items
//        onView(withId(R.id.rv_comments)).check(new RecyclerViewItemCount());
//
//        // Execute a same api call as the actual app to simulate the delay time
//        dataSource.getComments(commentsToPull).subscribe(comments -> latch.countDown(), error -> {});
//        latch.await();
//
//        // Click first comments's "View Replies" on the list (might need to change
//        // position if first comment has no replies, hence the commentPos variable).
//        while (!replyClicked && commentPos < commentsCount) {
//            onView(withId(R.id.rv_comments))
//                    .perform(RecyclerViewActions.actionOnItemAtPosition(commentPos, ClickAction.clickChildViewWithId(R.id.tv_view_replies)));
//        }
//
//        // Check that RepliesFragment is displayed if "View Replies" was clicked
//        if (replyClicked)
//            onView(withId(R.id.ll_replies)).check(matches(isDisplayed()));
//
//        // Try to click on Load More button for more replies. Will not click if Load More is not available.
//        try {
//            onView(withId(R.id.tv_load_more_replies)).perform(click());
//            //view is displayed logic
//        } catch (Exception e) {
//            //view not displayed logic
//        }
//
//        Fragment displayFragment = mainActivity.getSupportFragmentManager().findFragmentById(R.id.fl_main);
//        if (displayFragment instanceof RepliesFragment) {
//            try {
//                // Check that empty reply layout is displayed when onFecthStoriesError() method is called
//                runOnUiThread(((RepliesFragment) displayFragment)::onFecthStoriesError);
//                onView(withId(R.id.ll_empty_replies)).check(matches(isDisplayed()));
//
//                // Check that load more button is displayed when showLoadMore() method is called
//                runOnUiThread(((RepliesFragment) displayFragment)::showLoadMore);
//                onView(withId(R.id.tv_load_more_replies)).check(matches(isDisplayed()));
//
//                // Click on navigation up button to return to CommentsFragment
//                onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
//
//                // Check that RepliesFragment is not displayed
//                onView(withId(R.id.ll_replies)).check(doesNotExist());
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        }
//
//        // Check that CommentsFragment is displayed
//        onView(withId(R.id.ll_comments)).check(matches(isDisplayed()));
//
//        // Try to click on Load More button for more comments. Will not click if Load More is not available.
//        try {
//            onView(withId(R.id.tv_load_more_comments)).perform(click());
//            //view is displayed logic
//        } catch (Exception e) {
//            //view not displayed logic
//        }
//
//        displayFragment = mainActivity.getSupportFragmentManager().findFragmentById(R.id.fl_main);
//        if (displayFragment instanceof CommentsFragment) {
//            try {
//                // Check that empty comment layout is displayed when onFecthStoriesError() method is called
//                runOnUiThread(((CommentsFragment) displayFragment)::onFecthStoriesError);
//                onView(withId(R.id.ll_empty_comments)).check(matches(isDisplayed()));
//
//                // Check that load more button is displayed when showLoadMore() method is called
//                runOnUiThread(((CommentsFragment) displayFragment)::showLoadMore);
//                onView(withId(R.id.tv_load_more_comments)).check(matches(isDisplayed()));
//
//                // Click on navigation up button to return to TopStoriesFragment
//                onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
//
//                // Check that CommentsFragment is not displayed
//                onView(withId(R.id.ll_comments)).check(doesNotExist());
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        }
//
//        // Check that TopStoriesFragment is displayed
//        onView(withId(R.id.ll_stories)).check(matches(isDisplayed()));
//
//        // Click on story item to launch browser
//        Intents.init();
//        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_VIEW));
//        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));
//        onView(withId(R.id.rv_top_stories))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//        intended(expectedIntent);
//        Intents.release();
//
//        // Try to click on Load More button for more stories. Will not click if Load More is not available.
//        try {
//            onView(withId(R.id.tv_load_more_stories)).perform(click());
//            //view is displayed logic
//        } catch (Exception e) {
//            //view not displayed logic
//        }
//
//        displayFragment = mainActivity.getSupportFragmentManager().findFragmentById(R.id.fl_main);
//        if (displayFragment instanceof TopStoriesFragment) {
//            try {
//                // Check that load more button is not displayed when onRefresh() method is called
//                runOnUiThread(((TopStoriesFragment) displayFragment)::onRefresh);
//                onView(withId(R.id.tv_load_more_stories)).check(matches(not(isDisplayed())));
//
//                // Check that empty story layout is displayed when onFecthStoriesError() method is called
//                runOnUiThread(((TopStoriesFragment) displayFragment)::onFecthStoriesError);
//                onView(withId(R.id.ll_empty_stories)).check(matches(isDisplayed()));
//
//                // Check that load more button is displayed when showLoadMore() method is called
//                runOnUiThread(((TopStoriesFragment) displayFragment)::showLoadMore);
//                onView(withId(R.id.tv_load_more_comments)).check(matches(isDisplayed()));
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        }
//    }
//
//    public static class ClickAction {
//
//        public static ViewAction clickChildViewWithId(final int id) {
//            return new ViewAction() {
//                @Override
//                public Matcher<View> getConstraints() {
//                    return null;
//                }
//
//                @Override
//                public String getDescription() {
//                    return "Click on a child view with specified id.";
//                }
//
//                @Override
//                public void perform(UiController uiController, View view) {
//                    View v = view.findViewById(id);
//
//                    switch (id) {
//                        case R.id.tv_comments:
//                            if (v.getVisibility() == View.VISIBLE) {
//                                v.performClick();
//                                commentClicked = true;
//                            } else
//                                storyPos++;
//                            break;
//                        case R.id.tv_view_replies:
//                            if (v.getVisibility() == View.VISIBLE) {
//                                v.performClick();
//                                replyClicked = true;
//                            } else
//                                commentPos++;
//                            break;
//                    }
//                }
//            };
//        }
//    }
//
//    public static class RecyclerViewItemCount implements ViewAssertion {
//
//        @Override
//        public void check(View view, NoMatchingViewException noViewFoundException) {
//            if (noViewFoundException != null) {
//                throw noViewFoundException;
//            }
//
//            RecyclerView recyclerView = (RecyclerView) view;
//            RecyclerView.Adapter adapter = recyclerView.getAdapter();
//            switch(view.getId()) {
//                case R.id.rv_top_stories:
//                    storiesCount = adapter.getItemCount();
//                    break;
//                case R.id.rv_comments:
//                    commentsCount = adapter.getItemCount();
//                    break;
//            }
//        }
//    }
}
