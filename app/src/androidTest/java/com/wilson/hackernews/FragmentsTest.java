package com.wilson.hackernews;

import android.app.Activity;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.wilson.hackernews.activity.MainActivity;
import com.wilson.hackernews.adapter.CommentsAdapter;
import com.wilson.hackernews.fragment.CommentsFragment;
import com.wilson.hackernews.model.HackerNewsComment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class FragmentsTest extends ActivityInstrumentationTestCase2<TestActivity> {

    private CommentsFragment commentsFragment;

    @Rule
    public ActivityTestRule<MainActivity> testActivity = new ActivityTestRule<>(
            MainActivity.class);
//    ,
//            true,
//            // false: do not launch the activity immediately
//            false);

    @Before
    public void setup() {
        String commentID = "15702188";
        String[] commentsID = new String[] {commentID};
        commentsFragment = CommentsFragment.newInstance(commentsID);
        testActivity.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_main, commentsFragment, null).commit();
    }

    public FragmentsTest() {
        super(TestActivity.class);
    }

    private static class TestFragmentActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            LinearLayout view = new LinearLayout(this);
            view.setId(1);
            setContentView(view);
        }
    }

    @Test
    public void CommentEmptyKidsAdapterTest() {
        HackerNewsComment comment = new Gson().fromJson("{\"by\" : \"iamleppert\", \"id\" : 15702188, \"kids\" : [ ],\"parent\" : 15701417, \"text\" : \"Anyone who has worked in real web development knows that source maps are barely functional. When they do work, they are slow and there&#x27;s a non-trivial delay, during which time the minified source or transpiled source or whatever is shown.<p>There&#x27;s a 50&#x2F;50 shot they won&#x27;t even work at all, and of that 50% of the time they do work, they often don&#x27;t have the correct line and column.<p>Again, things like the interactive debugger and &quot;pause on caught exceptions&quot; often casually break with source maps.<p>In short, it&#x27;s a mess.\", \"time\" : 1510732926, \"type\" : \"comment\"}", HackerNewsComment.class);
        HackerNewsComment comment2 = new Gson().fromJson("{\"by\" : \"jschorr\", \"id\" : 15705190, \"kids\" : [ ], \"parent\" : 15701417, \"text\" : \"Some interesting history of source maps: They actually are older than the article suggests, having been original developed and used internally at Google starting in early 2009. At the time, Google had some of the largest applications written in JavaScript running inside of the browser, optimized and minified using Closure Compiler [1]. One focus of Closure Compiler was&#x2F;is minimizing bytes sent over wire, which resulted in trying to debug issues that occurred on Line 1, Character XXXXX of the minified file. As an engineer working 20% on a Gmail feature, I grew incredibly frustrated with...\",\"time\" : 1510764749,\"type\" : \"comment\"}", HackerNewsComment.class);
        List<HackerNewsComment> hackerNewsCommentList = new ArrayList<>(Arrays.asList(comment, comment2));

        CommentsAdapter.RepliesClickedListener delegate = repliesID -> {};
        CommentsAdapter commentsAdapter = new CommentsAdapter(delegate);
        commentsAdapter.setComments(hackerNewsCommentList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView commentsRV = (RecyclerView) commentsFragment.getView().findViewById(R.id.rv_comments);
        commentsRV.setLayoutManager(mLayoutManager);
        commentsRV.setAdapter(commentsAdapter);
        onView(withText(R.string.comment_view_replies)).check(matches(not(isDisplayed())));
        //Intent intent = new Intent();
        //testActivity.launchActivity(intent);

        //testActivity.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_main, topStoriesFragment, null).commit();
        //topStoriesFragment.showLoadMore();
        //onView(withId(R.id.tv_load_more_comments)).check(matches(isDisplayed()));
        //topStoriesFragment.commentsClicked(null);
        //onView(withText(R.string.comment_delegate_error)).inRoot(withDecorView(not(getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
}
