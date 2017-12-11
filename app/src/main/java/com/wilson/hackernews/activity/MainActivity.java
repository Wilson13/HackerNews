package com.wilson.hackernews.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.wilson.hackernews.R;
import com.wilson.hackernews.fragment.CommentsFragment;
import com.wilson.hackernews.fragment.RepliesFragment;
import com.wilson.hackernews.fragment.TopStoriesFragment;
import com.wilson.hackernews.other.MyApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements TopStoriesFragment.TopStoriesListener, CommentsFragment.CommentsListener, FragmentManager.OnBackStackChangedListener {

    private static final String TAG_TOP_STORIES_FRAGMENT = "TopStoriesFragment";
    private static final String TAG_COMMENTS_FRAGMENT = "CommentsFragment";
    private static final String TAG_REPLIES_FRAGMENT = "RepliesFragment";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MyApp.getAppComponent().inject(this);

        // Set up toolbar
        setSupportActionBar(toolbar);

        // Set up BackStackChangedListener
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fl_main) != null) {

            // Check if TopStoriesFragment is in stack, if not, create new fragment.
            FragmentManager fm = getSupportFragmentManager();
            TopStoriesFragment topStoriesFragment = (TopStoriesFragment) fm.findFragmentByTag(TAG_TOP_STORIES_FRAGMENT);

            // Not restored from previous state
            if (topStoriesFragment == null) {//savedInstanceState == null) {
                Log.d("MainActivity", "topStoriesFragment is null");
                showTopStories();
            } else {
                changeTitle();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // If it is not an instance of TopStoriesFragment, just pop fragment from stack.
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            // If only one Fragment left in stack (which is the CommentsFragment,
            // TopStoriesFragment will never be in the stack), hide back button since
            // we are popping it out, so TopStoriesFragment will be displayed.
            if (getSupportFragmentManager().getBackStackEntryCount() == 1)
                hideBackButton();

            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Should return true to consume the event
        // but let it go as there's no other MenuItem
        // , also no need for switch case as there's only one item.
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void showBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void hideBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void showTopStories() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, TopStoriesFragment.newInstance(), TAG_TOP_STORIES_FRAGMENT).commit();
        getSupportActionBar().setTitle(R.string.title_top_stories);
        hideBackButton();
    }

    @Override
    public void showComments(String[] commentsID) {
        // Add fragments to stack
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_main, CommentsFragment.newInstance(commentsID), TAG_COMMENTS_FRAGMENT);
        fragmentTransaction.addToBackStack(CommentsFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public void showReplies(String[] repliesID) {
        // Add fragments to stack
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_main, RepliesFragment.newInstance(repliesID), TAG_REPLIES_FRAGMENT);
        fragmentTransaction.addToBackStack(RepliesFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackStackChanged() {
        // Set correct title
        changeTitle();
    }

    private void changeTitle() {
        Fragment displayFragment = getSupportFragmentManager().findFragmentById(R.id.fl_main);

        // Set correct title
        if (displayFragment instanceof TopStoriesFragment) {
            getSupportActionBar().setTitle(R.string.title_top_stories);
            hideBackButton();
        }
        else if (displayFragment instanceof CommentsFragment) {
            getSupportActionBar().setTitle(R.string.title_comments);
            showBackButton();
        }
        else {
            getSupportActionBar().setTitle(R.string.title_replies);
            showBackButton();
        }
    }
}
