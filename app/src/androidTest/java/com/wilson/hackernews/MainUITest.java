package com.wilson.hackernews;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.wilson.hackernews.activity.MainActivity;
import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.mvp.HNStoriesPresenter;
import com.wilson.hackernews.mvp.HackerNewsModel;
import com.wilson.hackernews.other.Constants;
import com.wilson.hackernews.other.MyApp;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.reactivex.Observable;
import io.reactivex.Single;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MainUITest {

    @Mock
    HackerNewsModel dataSource;
    @InjectMocks
    private HNStoriesPresenter storiesPresenter = new HNStoriesPresenter(dataSource);

    private String storyID;
    private String storyID2;
    private String[] topStoriesID;
    private List<String> storiesToPullList = new ArrayList<>();
    private ArrayList hackerNewsStoryList;

    private HackerNewsStory story;
    private HackerNewsStory story2;

    private String commentID;
    private String commentID2;
    private String[] commentsID;
    private List<String> commentsToPullList = new ArrayList<>();
    private ArrayList hackerNewsCommentList;

    private HackerNewsComment comment;
    private HackerNewsComment comment2;

    static int storiesCount = 0;
    static int storyPos = 0;
    static boolean commentClicked = false;

    static int commentsCount = 0;
    static int commentPos = 0;
    static boolean replyClicked = false;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(
            MainActivity.class,
            true,
            // false: do not launch the activity immediately
            false);

    private final CountDownLatch latch = new CountDownLatch(1);
    private MockWebServer server;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        String storyResponse = "{\n" +
                "  \"by\" : \"heshamg\",\n" +
                "  \"descendants\" : 101,\n" +
                "  \"id\" : 15683275,\n" +
                "  \"kids\" : [ 15685177, 15684534, 15683718, 15685551, 15684947, 15684418, 15683523, 15683499, 15683566, 15683461, 15683463, 15684300, 15683579, 15683851, 15684266, 15684849, 15683674 ],\n" +
                "  \"score\" : 180,\n" +
                "  \"time\" : 1510531519,\n" +
                "  \"title\" : \"Uber confirms SoftBank has agreed to invest\",\n" +
                "  \"type\" : \"story\",\n" +
                "  \"url\" : \"https://techcrunch.com/2017/11/12/uber-confirms-softbank-has-agreed-to-invest-billions-in-uber/\"\n" +
                "}";
        //Set a response for retrofit to handle. You can copy a sample
        //response from your server to simulate a correct result or an error.
        //MockResponse can also be customized with different parameters
        //to match your test needs
        server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[ 15683275, 15683275, 15683275, 15683275, 15683275, 15683275, 15683275, 15683275, 15683275, 15683275, 15683275 ]"));
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
        server.play();

        String url = server.getUrl("/").toString();
        Constants.HACKER_NEWS_BASE_URL = url;
        storyID = "15683275";
        storyID2 = "15701417";
        topStoriesID = new String[] {storyID, storyID, storyID, storyID, storyID, storyID, storyID, storyID, storyID, storyID, storyID };
        storiesToPullList = new ArrayList<>(Arrays.asList(topStoriesID));
        storiesToPullList.remove(topStoriesID.length-1);

        story = new Gson().fromJson("{ \"by\" : \"heshamg\", \"descendants\" : 59, \"id\" : 15683275, \"kids\" : [ 15684947, 15684534, 15684418, 15683718, 15683523, 15683499, 15684849, 15683566, 15683461, 15683463, 15683579, 15684300, 15683851, 15684266, 15683674 ], \"score\" : 141, \"time\" : 1510531519, \"title\" : \"Uber confirms SoftBank has agreed to invest\", \"type\" : \"story\", \"url\" : \"https://techcrunch.com/2017/11/12/uber-confirms-softbank-has-agreed-to-invest-billions-in-uber/\"}", HackerNewsStory.class);
        story2 = new Gson().fromJson("{ \"by\" : \"aeontech\", \"descendants\" : 32, \"id\" : 15701417, \"kids\" : [ 15702188, 15702224, 15701593, 15701728, 15701764, 15701658, 15702192 ], \"score\" : 55, \"time\" : 1510719453, \"title\" : \"WTF is a Source Map\", \"type\" : \"story\", \"url\" : \"https://www.schneems.com/2017/11/14/wtf-is-a-source-map/\"}", HackerNewsStory.class);
        hackerNewsStoryList = new ArrayList<>(Arrays.asList(story, story, story, story, story, story, story, story, story, story));

        commentID = "15702188";
        commentID2 = "15705190";
        commentsID = new String[] { commentID, commentID, commentID, commentID, commentID, commentID, commentID, commentID, commentID, commentID, commentID };
        commentsToPullList = new ArrayList<>(Arrays.asList(commentsID));
        commentsToPullList.remove(commentsID.length-1);

        comment = new Gson().fromJson("{\"by\" : \"iamleppert\", \"id\" : 15702188, \"kids\" : [ 15702419, 15702707, 15702622, 15705397, 15702670 ],\"parent\" : 15701417, \"text\" : \"Anyone who has worked in real web development knows that source maps are barely functional. When they do work, they are slow and there&#x27;s a non-trivial delay, during which time the minified source or transpiled source or whatever is shown.<p>There&#x27;s a 50&#x2F;50 shot they won&#x27;t even work at all, and of that 50% of the time they do work, they often don&#x27;t have the correct line and column.<p>Again, things like the interactive debugger and &quot;pause on caught exceptions&quot; often casually break with source maps.<p>In short, it&#x27;s a mess.\", \"time\" : 1510732926, \"type\" : \"comment\"}", HackerNewsComment.class);
        comment2 = new Gson().fromJson("{\"by\" : \"jschorr\", \"id\" : 15705190, \"kids\" : [ 15706721, 15705436 ], \"parent\" : 15701417, \"text\" : \"Some interesting history of source maps: They actually are older than the article suggests, having been original developed and used internally at Google starting in early 2009. At the time, Google had some of the largest applications written in JavaScript running inside of the browser, optimized and minified using Closure Compiler [1]. One focus of Closure Compiler was&#x2F;is minimizing bytes sent over wire, which resulted in trying to debug issues that occurred on Line 1, Character XXXXX of the minified file. As an engineer working 20% on a Gmail feature, I grew incredibly frustrated with...\",\"time\" : 1510764749,\"type\" : \"comment\"}", HackerNewsComment.class);
        hackerNewsCommentList = new ArrayList<>(Arrays.asList(comment, comment, comment, comment, comment, comment, comment, comment, comment, comment));

        when(dataSource.getTopStories()).thenReturn(Single.fromObservable(Observable.just(topStoriesID)));
        when(dataSource.getStories(storiesToPullList)).thenReturn(Observable.fromIterable(storiesToPullList).flatMap(id -> dataSource.getStory(id)).toList());
        when(dataSource.getStory(storyID)).thenReturn(Observable.just(story));

        when(dataSource.getComment(commentID)).thenReturn(Observable.just(comment));
        when(dataSource.getComments(commentsToPullList)).thenReturn(Observable.fromIterable(commentsToPullList).flatMap(id -> dataSource.getComment(id)).toList());
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
    public void UITest() throws InterruptedException, IOException {
        //String[] commentsID = new String[] { "15752121", "15752604", "15755688", "15752991", "15752062", "15752142", "15752182", "15752094", "15754139", "15752293", "15753578", "15752053", "15752069", "15752160", "15753740", "15755865", "15752349", "15752345", "15752356", "15755288", "15752098", "15752101", "15754579", "15753457", "15754523", "15756145", "15755126", "15752953", "15754722", "15753179", "15754622", "15756146", "15752556", "15752306", "15752201", "15753644", "15753172", "15754573", "15753007", "15752482", "15753626", "15753636", "15752731", "15752790", "15753250", "15752763", "15753733", "15753316", "15755150", "15752254", "15753996", "15755281", "15753760", "15753324", "15753683", "15752124", "15753861", "15752527", "15753956", "15753779", "15753646", "15753407", "15753093", "15752573", "15752079", "15752277", "15753769", "15753334", "15754836", "15754086", "15755912", "15752224", "15752504", "15752113", "15752155" };
        //ArrayList commentsToPull = new ArrayList<>(Arrays.asList(commentsID));

        // Execute a same api call as the actual app to simulate the delay time.
        // (should have used Espresso Idle Resource but for simplicity's sake, use this method here)
        //GetHackerNewsContract.APIModel apiModel = RetrofitSingleton.getClient(HACKER_NEWS_BASE_URL).create(GetHackerNewsContract.APIModel.class);
        //HackerNewsModel dataSource = new HackerNewsModel(apiModel);

        // Execute a same api call as the actual app to simulate the delay time
        //dataSource.getTopStories().subscribe(hackerNewsStories -> latch.countDown(), error -> {});
        //latch.await();

        Intent intent = new Intent();
        mainActivity.launchActivity(intent);

        //TopStoriesFragment topStoriesFragment = TopStoriesFragment.newInstance();
        //topStoriesFragment.presenter = storiesPresenter;
        //mainActivity.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, topStoriesFragment, null).commit();

        // Check that TopStoriesFragment is displayed
        onView(withId(R.id.ll_stories)).check(matches(isDisplayed()));

        // Check that empty stories layout is not displayed
        onView(withId(R.id.ll_empty_stories)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        // Get number of story items
        onView(withId(R.id.rv_top_stories)).check(new RecyclerViewItemCount());

        // Click first story's comments on the list (might need to change
        // position if first story has no comments, hence the storyPOS variable).
        while (!commentClicked && storyPos < storiesCount) {
            onView(withId(R.id.rv_top_stories))
                .perform(RecyclerViewActions.actionOnItemAtPosition(storyPos, ClickAction.clickChildViewWithId(R.id.tv_comments)));
        }

        while(true)
            ;
        // Check that CommentsFragment is displayed if "Comments" was clicked
        //if (commentClicked)
            //onView(withId(R.id.ll_comments)).check(matches(isDisplayed()));

        /*// Get number of comment items
        onView(withId(R.id.rv_comments)).check(new RecyclerViewItemCount());

        // Execute a same api call as the actual app to simulate the delay time
        //latch.await();

        // Click first comments's "View Replies" on the list (might need to change
        // position if first comment has no replies, hence the commentPos variable).
        while (!replyClicked && commentPos < commentsCount) {
            onView(withId(R.id.rv_comments))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(commentPos, ClickAction.clickChildViewWithId(R.id.tv_view_replies)));
        }

        // Check that RepliesFragment is displayed if "View Replies" was clicked
        if (replyClicked)
            onView(withId(R.id.ll_replies)).check(matches(isDisplayed()));

        // Try to click on Load More button for more replies. Will not click if Load More is not available.
        try {
            onView(withId(R.id.tv_load_more_replies)).perform(click());
            //view is displayed logic
        } catch (Exception e) {
            //view not displayed logic
        }

        Fragment displayFragment = mainActivity.getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_main);
        if (displayFragment instanceof RepliesFragment) {
            try {
                // Check that empty reply layout is displayed when onFecthStoriesError() method is called
                runOnUiThread(((RepliesFragment) displayFragment)::onFecthStoriesError);
                onView(withId(R.id.ll_empty_replies)).check(matches(isDisplayed()));

                // Check that load more button is displayed when showLoadMore() method is called
                runOnUiThread(((RepliesFragment) displayFragment)::showLoadMore);
                onView(withId(R.id.tv_load_more_replies)).check(matches(isDisplayed()));

                // Click on navigation up button to return to CommentsFragment
                onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());

                // Check that RepliesFragment is not displayed
                onView(withId(R.id.ll_replies)).check(doesNotExist());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        // Check that CommentsFragment is displayed
        onView(withId(R.id.ll_comments)).check(matches(isDisplayed()));

        // Try to click on Load More button for more comments. Will not click if Load More is not available.
        try {
            onView(withId(R.id.tv_load_more_comments)).perform(click());
            //view is displayed logic
        } catch (Exception e) {
            //view not displayed logic
        }

        displayFragment = mainActivity.getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_main);
        if (displayFragment instanceof CommentsFragment) {
            try {
                // Check that empty comment layout is displayed when onFecthStoriesError() method is called
                runOnUiThread(((CommentsFragment) displayFragment)::onFecthStoriesError);
                onView(withId(R.id.ll_empty_comments)).check(matches(isDisplayed()));

                // Check that load more button is displayed when showLoadMore() method is called
                runOnUiThread(((CommentsFragment) displayFragment)::showLoadMore);
                onView(withId(R.id.tv_load_more_comments)).check(matches(isDisplayed()));

                // Click on navigation up button to return to TopStoriesFragment
                onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());

                // Check that CommentsFragment is not displayed
                onView(withId(R.id.ll_comments)).check(doesNotExist());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

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

        // Try to click on Load More button for more stories. Will not click if Load More is not available.
        try {
            onView(withId(R.id.tv_load_more_stories)).perform(click());
            //view is displayed logic
        } catch (Exception e) {
            //view not displayed logic
        }

        displayFragment = mainActivity.getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_main);
        if (displayFragment instanceof TopStoriesFragment) {
            try {
                // Check that load more button is not displayed when onRefresh() method is called
                runOnUiThread(((TopStoriesFragment) displayFragment)::onRefresh);
                onView(withId(R.id.tv_load_more_stories)).check(matches(not(isDisplayed())));

                // Check that empty story layout is displayed when onFecthStoriesError() method is called
                runOnUiThread(((TopStoriesFragment) displayFragment)::onFecthStoriesError);
                onView(withId(R.id.ll_empty_stories)).check(matches(isDisplayed()));

                // Check that load more button is displayed when showLoadMore() method is called
                runOnUiThread(((TopStoriesFragment) displayFragment)::showLoadMore);
                onView(withId(R.id.tv_load_more_comments)).check(matches(isDisplayed()));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }*/
    }

    public static class ClickAction {

        public static ViewAction clickChildViewWithId(final int id) {
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
                            if (v.getVisibility() == View.VISIBLE) {
                                v.performClick();
                                commentClicked = true;
                            } else
                                storyPos++;
                            break;
                        case R.id.tv_view_replies:
                            if (v.getVisibility() == View.VISIBLE) {
                                v.performClick();
                                replyClicked = true;
                            } else
                                commentPos++;
                            break;
                    }
                }
            };
        }
    }

    public static class RecyclerViewItemCount implements ViewAssertion {

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            switch(view.getId()) {
                case R.id.rv_top_stories:
                    storiesCount = adapter.getItemCount();
                    break;
                case R.id.rv_comments:
                    commentsCount = adapter.getItemCount();
                    break;
            }
        }
    }
}
