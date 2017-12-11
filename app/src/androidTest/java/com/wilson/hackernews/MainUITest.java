package com.wilson.hackernews;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.core.internal.deps.guava.collect.Iterables;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.wilson.hackernews.activity.MainActivity;
import com.wilson.hackernews.fragment.CommentsFragment;
import com.wilson.hackernews.fragment.RepliesFragment;
import com.wilson.hackernews.fragment.TopStoriesFragment;
import com.wilson.hackernews.other.Constants;
import com.wilson.hackernews.other.MyApp;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainUITest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(
            MainActivity.class,
            true,
            // false: do not launch the activity immediately
            false);

    @Before
    public void setup() throws IOException {

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
    public void MainUITest() throws Throwable {

        String storyResponse = "{\n" +
                "  \"by\" : \"heshamg\",\n" +
                "  \"descendants\" : 101,\n" +
                "  \"id\" : 15683275,\n" +
                "  \"kids\" : [ 15685177, 15685177, 15685177, 15685177, 15685177, 15685177, 15685177, 15685177, 15685177, 15685177, 15685177 ],\n" +
                "  \"score\" : 180,\n" +
                "  \"time\" : 1510531519,\n" +
                "  \"title\" : \"Uber confirms SoftBank has agreed to invest\",\n" +
                "  \"type\" : \"story\",\n" +
                "  \"url\" : \"https://techcrunch.com/2017/11/12/uber-confirms-softbank-has-agreed-to-invest-billions-in-uber/\"\n" +
                "}";

        String oneKidStoryResponse = "{\n" +
                "  \"by\" : \"heshamg\",\n" +
                "  \"descendants\" : 101,\n" +
                "  \"id\" : 15683275,\n" +
                "  \"kids\" : [ 15685177 ],\n" +
                "  \"score\" : 180,\n" +
                "  \"time\" : 1510531519,\n" +
                "  \"title\" : \"Uber confirms SoftBank has agreed to invest\",\n" +
                "  \"type\" : \"story\",\n" +
                "  \"url\" : \"https://techcrunch.com/2017/11/12/uber-confirms-softbank-has-agreed-to-invest-billions-in-uber/\"\n" +
                "}";

        String emptyKidStoryResponse = "{\n" +
                "  \"by\" : \"heshamg\",\n" +
                "  \"descendants\" : 101,\n" +
                "  \"id\" : 15683275,\n" +
                "  \"kids\" : [ ],\n" +
                "  \"score\" : 180,\n" +
                "  \"time\" : 1510531519,\n" +
                "  \"title\" : \"Uber confirms SoftBank has agreed to invest\",\n" +
                "  \"type\" : \"story\",\n" +
                "  \"url\" : \"https://techcrunch.com/2017/11/12/uber-confirms-softbank-has-agreed-to-invest-billions-in-uber/\"\n" +
                "}";

        String commentResponse = "{\n" +
                "  \"by\" : \"nopinsight\",\n" +
                "  \"id\" : 15685177,\n" +
                "  \"kids\" : [ 15685661, 15685661, 15685661, 15685661, 15685661, 15685661, 15685661, 15685661, 15685661, 15685661, 15685661 ],\n" +
                "  \"parent\" : 15683275,\n" +
                "  \"text\" : \"When I look at their runway and valuation going forward, IPO is the most probable path they will take. But it might imply some risks to the overall startup IPO scene in the future as well.<p>Apparently Uber is bleeding about $2 billion a year and have only $6.6 billion in cash reserve. If this deal adds $1 billion more cash, then that would only last 4 years.<p>Since full-scale self-driving cars launching across most major cities is unlikely to happen within 4 years, they will need to either:<p>1) Charge more and lose even more market share to Lyft. This is unlikely.<p>2) Raise more cash. IPO seems to be the easiest channel for them now. (Maybe SoftBank will provide it from their massive fund but they might also extract concessions from existing shareholders and buy at a very ‘attractive’ price (for SoftBank) but others will likely resist.)<p>IPO sounds like a good plan for Uber. But if the price drops precipitously at some point afterward because it is outcompeted by other self-driving companies (e.g. Waymo + Lyft expands self-driving fleets to highly profitable market while Uber’s tech is still not ready) it may result in a chilling effect on the overall startup IPO scene as they are the largest unicorn in existence, and there are many startups that sell themselves as Uber for X. (Public investors may not be as astute as VCs in discerning differences in business models of similar sounding startups.)\",\n" +
                "  \"time\" : 1510562771,\n" +
                "  \"type\" : \"comment\"\n" +
                "}" +
                "";

        String emptyKidsCommentResponse = "{\n" +
                "  \"by\" : \"nopinsight\",\n" +
                "  \"id\" : 15685177,\n" +
                "  \"kids\" : [ ],\n" +
                "  \"parent\" : 15683275,\n" +
                "  \"text\" : \"When I look at their runway and valuation going forward, IPO is the most probable path they will take. But it might imply some risks to the overall startup IPO scene in the future as well.<p>Apparently Uber is bleeding about $2 billion a year and have only $6.6 billion in cash reserve. If this deal adds $1 billion more cash, then that would only last 4 years.<p>Since full-scale self-driving cars launching across most major cities is unlikely to happen within 4 years, they will need to either:<p>1) Charge more and lose even more market share to Lyft. This is unlikely.<p>2) Raise more cash. IPO seems to be the easiest channel for them now. (Maybe SoftBank will provide it from their massive fund but they might also extract concessions from existing shareholders and buy at a very ‘attractive’ price (for SoftBank) but others will likely resist.)<p>IPO sounds like a good plan for Uber. But if the price drops precipitously at some point afterward because it is outcompeted by other self-driving companies (e.g. Waymo + Lyft expands self-driving fleets to highly profitable market while Uber’s tech is still not ready) it may result in a chilling effect on the overall startup IPO scene as they are the largest unicorn in existence, and there are many startups that sell themselves as Uber for X. (Public investors may not be as astute as VCs in discerning differences in business models of similar sounding startups.)\",\n" +
                "  \"time\" : 1510562771,\n" +
                "  \"type\" : \"comment\"\n" +
                "}" +
                "";

        String replyResponse = "{\n" +
                "  \"by\" : \"loceng\",\n" +
                "  \"id\" : 15685661,\n" +
                "  \"kids\" : [ 15685906, 15687216, 15686823 ],\n" +
                "  \"parent\" : 15685177,\n" +
                "  \"text\" : \"IPO is good for Uber - not for society though, who will be the ones to capture the losses.<p>Also, Tesla often is brought up in this competition - usually mentioned between Uber and Lyft and Chinese companies - however Tesla is positioned to dominate here. I wouldn&#x27;t doubt that they&#x27;re working on the apps internally to create the shared vehicle system that Elon speaks of. How many vehicles could Tesla create AT COST spending the $2B &#x2F; year that Uber is burning? Where Tesla has a fleet of assets on the road ...\",\n" +
                "  \"time\" : 1510571511,\n" +
                "  \"type\" : \"comment\"\n" +
                "}";
        //Set a response for retrofit to handle.
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[ 15683275, 15683275, 15683275, 15683275, 15683275, 15683275, 15683275, 15683275, 15683275, 15683275, 15683275 ]"));
        // 10 responses + 1 more for load more function
        server.enqueue(new MockResponse().setBody(storyResponse));
        server.enqueue(new MockResponse().setBody(storyResponse));
        server.enqueue(new MockResponse().setBody(storyResponse));
        server.enqueue(new MockResponse().setBody(storyResponse));
        server.enqueue(new MockResponse().setBody(storyResponse));
        server.enqueue(new MockResponse().setBody(storyResponse));
        server.enqueue(new MockResponse().setBody(storyResponse));
        server.enqueue(new MockResponse().setBody(storyResponse));
        server.enqueue(new MockResponse().setBody(storyResponse));
        server.enqueue(new MockResponse().setBody(storyResponse));
        server.enqueue(new MockResponse().setBody(storyResponse));

        // 10 responses + 1 more for load more function
        server.enqueue(new MockResponse().setBody(commentResponse));
        server.enqueue(new MockResponse().setBody(commentResponse));
        server.enqueue(new MockResponse().setBody(commentResponse));
        server.enqueue(new MockResponse().setBody(commentResponse));
        server.enqueue(new MockResponse().setBody(commentResponse));
        server.enqueue(new MockResponse().setBody(commentResponse));
        server.enqueue(new MockResponse().setBody(commentResponse));
        server.enqueue(new MockResponse().setBody(commentResponse));
        server.enqueue(new MockResponse().setBody(commentResponse));
        server.enqueue(new MockResponse().setBody(commentResponse));
        server.enqueue(new MockResponse().setBody(commentResponse));

        // 10 responses + 1 more for load more function
        server.enqueue(new MockResponse().setBody(replyResponse));
        server.enqueue(new MockResponse().setBody(replyResponse));
        server.enqueue(new MockResponse().setBody(replyResponse));
        server.enqueue(new MockResponse().setBody(replyResponse));
        server.enqueue(new MockResponse().setBody(replyResponse));
        server.enqueue(new MockResponse().setBody(replyResponse));
        server.enqueue(new MockResponse().setBody(replyResponse));
        server.enqueue(new MockResponse().setBody(replyResponse));
        server.enqueue(new MockResponse().setBody(replyResponse));
        server.enqueue(new MockResponse().setBody(replyResponse));
        server.enqueue(new MockResponse().setBody(replyResponse));

        // Called once for checking that load more button is not displayed when onRefresh() method is called
        //server.enqueue(new MockResponse().setBody("[ 15683275 ]"));
        //server.enqueue(new MockResponse().setBody(oneKidStoryResponse));
        //server.enqueue(new MockResponse().setBody(emptyKidsCommentResponse));

        //server.enqueue(new MockResponse().setBody("[ 15683275 ]"));
        //server.enqueue(new MockResponse().setBody(oneKidStoryResponse));
        //server.enqueue(new MockResponse().setBody(emptyKidsCommentResponse));

        // Called once more for reloading stories into layout
        server.enqueue(new MockResponse().setBody("[ 15683275 ]"));
        // Return one story with comments to click on
        server.enqueue(new MockResponse().setBody(oneKidStoryResponse));
        // Return one comment with no replies to click on
        server.enqueue(new MockResponse().setBody(emptyKidsCommentResponse));

        // Called again for reloading stories into layout
        server.enqueue(new MockResponse().setBody("[ 15683275 ]"));
        // Return one story with no comments to click on
        server.enqueue(new MockResponse().setBody(emptyKidStoryResponse));

        // Start MockWebServer
        server.play();

        String url = server.getUrl("/").toString();
        Constants.HACKER_NEWS_BASE_URL = url;

        Intent intent = new Intent();
        mainActivity.launchActivity(intent);

        // Check that TopStoriesFragment is displayed
        onView(withId(R.id.ll_stories)).check(matches(isDisplayed()));

        // Check that empty stories layout is not displayed
        onView(withId(R.id.ll_empty_stories)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        // Click on "Load more" for more stories
        onView(withId(R.id.tv_load_more_stories)).perform(click());

        // Check that "{N} comments" button was displayed
        //onView(allOf(withId(R.id.tv_comments), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).check(matches(isDisplayed()));

        onView(withId(R.id.rv_top_stories))
                .check(matches(allOf(hasDescendant(withId(R.id.tv_comments)), isDisplayed())));

        // Click first story's comments on the list
        onView(withId(R.id.rv_top_stories))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ClickAction.clickChildViewWithId(R.id.tv_comments)));

        // Check that CommentsFragment is displayed when "Comments" was clicked
        onView(withId(R.id.ll_comments)).check(matches(isDisplayed()));

        // Test screen rotation on CommentsFragment
        rotateScreen();

        onView(withId(R.id.tv_view_replies)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Restore screen orientation
        rotateScreen();

        // Click on "Load more" for more comments
        onView(withId(R.id.tv_load_more_comments)).perform(click());

        // Click first comments's "View Replies" on the list
        onView(withId(R.id.rv_comments))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ClickAction.clickChildViewWithId(R.id.tv_view_replies)));

        // Check that RepliesFragment is displayed if "View Replies" was clicked
        onView(withId(R.id.ll_replies)).check(matches(isDisplayed()));

        // Test screen rotation on RepliesFragment
        rotateScreen();

        onView(withId(R.id.rv_replies)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Restore screen orientation
        rotateScreen();

        // Click on "Load more" for more replies
        onView(withId(R.id.tv_load_more_replies)).perform(click());

        Fragment displayFragment = getCurrentActivity().getSupportFragmentManager().findFragmentById(R.id.fl_main);

        if (displayFragment instanceof RepliesFragment) {

            // Check that toolbar is displayed
            onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
            // Check that toolbar title is correct
            onView(withText(R.string.title_replies)).check(matches(withParent(withId(R.id.toolbar))));

            // Check that load more button is displayed when showLoadMore() method is called
            runOnUiThread(((RepliesFragment) displayFragment)::showLoadMore);
            onView(withId(R.id.tv_load_more_replies)).check(matches(isDisplayed()));

            // Check that empty reply layout is displayed when onFetchStoriesError() method is called
            runOnUiThread(((RepliesFragment) displayFragment)::onFecthStoriesError);
            onView(withId(R.id.ll_empty_replies)).check(matches(isDisplayed()));
        }

        // Click on navigation up button to return to CommentsFragment
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());

        // Check that RepliesFragment is not displayed
        onView(withId(R.id.ll_replies)).check(doesNotExist());

        // Check that CommentsFragment is displayed
        onView(withId(R.id.ll_comments)).check(matches(isDisplayed()));

        displayFragment = getCurrentActivity().getSupportFragmentManager().findFragmentById(R.id.fl_main);

        if (displayFragment instanceof CommentsFragment) {

            // Check that load more button is displayed when showLoadMore() method is called
            runOnUiThread(((CommentsFragment) displayFragment)::showLoadMore);
            onView(withId(R.id.tv_load_more_comments)).check(matches(isDisplayed()));

            // Check that empty comment layout is displayed when onFetchStoriesError() method is called
            runOnUiThread(((CommentsFragment) displayFragment)::onFecthStoriesError);
            onView(withId(R.id.ll_empty_comments)).check(matches(isDisplayed()));
        }

        // Click on navigation up button to return to TopStoriesFragment
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());

        // Check that CommentsFragment is not displayed
        onView(withId(R.id.ll_comments)).check(doesNotExist());

        // Check that TopStoriesFragment is displayed
        onView(withId(R.id.ll_stories)).check(matches(isDisplayed()));

        // Click on story item to launch browser
        Intents.init();
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_VIEW));
        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));
        onView(withId(R.id.rv_top_stories))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(expectedIntent);
        Intents.release();

        displayFragment = getCurrentActivity().getSupportFragmentManager().findFragmentById(R.id.fl_main);

        if (displayFragment instanceof TopStoriesFragment) {

            // Check that load more button is displayed when showLoadMore() method is called
            runOnUiThread(((TopStoriesFragment) displayFragment)::showLoadMore);
            onView(withId(R.id.tv_load_more_stories)).check(matches(isDisplayed()));

            // Check that load more button is not displayed when hideLoadMore() method is called
            runOnUiThread(((TopStoriesFragment) displayFragment)::hideLoadMore);
            onView(withId(R.id.tv_load_more_stories)).check(matches(not(isDisplayed())));

            // Check that empty story layout is displayed when onFetchStoriesError() method is called
            runOnUiThread(((TopStoriesFragment) displayFragment)::onFetchStoriesError);
            onView(withId(R.id.ll_empty_stories)).check(matches(isDisplayed()));
        }

        // This sleep is required for the refresh to finish
        //Thread.sleep(2000);

        // Couldn't break these tests into different files as they all rely on
        // same activity, running them together in separate test files would fail.

        //Reload stories (calling onRefresh directly cause error, not sure about the reason)

        /*Fragment finalDisplayFragment = displayFragment;
        Runnable myRunnable = new Runnable(){
            @Override
            public void run(){
                // What you want to run //
                ((TopStoriesFragment) finalDisplayFragment).onRefresh();
            }
        };*/

        //runOnUiThread(myRunnable);
        //InstrumentationRegistry.getInstrumentation().waitForIdle(myRunnable);
        onView(withId(R.id.srl_top_stories))
                .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));

        // Check that load more button is not displayed when onRefresh() method is called
        //runOnUiThread(((TopStoriesFragment) displayFragment)::onRefresh);
        //onView(withId(R.id.tv_load_more_stories)).check(matches(not(isDisplayed())));

        // This sleep is required for the refresh to finish
        //Thread.sleep(3000);

        // Check that TopStoriesFragment is displayed
        onView(withId(R.id.ll_stories)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Click first story's comments on the list
        onView(withId(R.id.rv_top_stories))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ClickAction.clickChildViewWithId(R.id.tv_comments)));

        // This sleep is required for the load to finish
        //Thread.sleep(2000);

        // Check that CommentsFragment is displayed
        onView(withId(R.id.ll_comments)).check(matches(isDisplayed()));

        // Check that no "View replies" button was displayed
        onView(withId(R.id.tv_view_replies)).check(matches(not(isDisplayed())));

        // Click on navigation up button to return to TopStoriesFragment
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());

        // Test screen rotation on TopStoriesFragment
        rotateScreen();

        onView(withId(R.id.tv_comments)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Restore screen orientation
        rotateScreen();

        // Check that load more button is not displayed when onRefresh() method is called
        //runOnUiThread(((TopStoriesFragment) displayFragment)::onRefresh);
        onView(withId(R.id.srl_top_stories))
                .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(95)));

        // This sleep is required for the refresh to finish
        Thread.sleep(2000);

        // Check that no "{N} comments" button was displayed
        onView(withId(R.id.tv_comments)).check(matches(not(isDisplayed())));
    }

    private void rotateScreen() {
        Context context = InstrumentationRegistry.getTargetContext();
        int orientation = context.getResources().getConfiguration().orientation;

        Activity activity = mainActivity.getActivity();
        activity.setRequestedOrientation(
                (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    // mainActivity.getActivity() would not return the
    // current activity and would cause problem after screen
    // rotation as Android destroy and create new activity
    private MainActivity getCurrentActivity() throws Throwable {
        getInstrumentation().waitForIdleSync();
        final MainActivity[] activity = new MainActivity[1];
        runOnUiThread(() -> {
            java.util.Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
            activity[0] = (MainActivity) Iterables.getOnlyElement(activities);
        });
        return activity[0];
    }

    public static class ClickAction {

        static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);

                    switch (id) {
                        case R.id.tv_comments:
                            if (v.getVisibility() == View.VISIBLE)
                                v.performClick();

                            break;
                        case R.id.tv_view_replies:
                            if (v.getVisibility() == View.VISIBLE)
                                v.performClick();
                            break;
                    }
                }
            };
        }
    }

    public static ViewAction withCustomConstraints(final ViewAction action, final Matcher<View> constraints) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return constraints;
            }

            @Override
            public String getDescription() {
                return action.getDescription();
            }

            @Override
            public void perform(UiController uiController, View view) {
                action.perform(uiController, view);
            }
        };
    }
}
