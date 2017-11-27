package com.wilson.hackernews;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.wilson.hackernews.activity.MainActivity;
import com.wilson.hackernews.fragment.CommentsFragment;
import com.wilson.hackernews.fragment.RepliesFragment;
import com.wilson.hackernews.fragment.TopStoriesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class TestActivity extends MainActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //  MyApp.getAppComponent().inject(this);

        // Set up toolbar
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        // If it is not an instance of TopStoriesFragment, just pop fragment from stack.
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            // If only one Fragment left in stack (which is the CommentsFragment,
            // TopStoriesFragment will never be in the stack), hide back button.
            if (getSupportFragmentManager().getBackStackEntryCount() == 1)
                hideBackButton();

            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Make back button in toolbar function properly
        switch (item.getItemId()){
            case android.R.id.home:
                // Should return true to consume the event
                // but let it go as there's no other MenuItem
                // and also for code coverage's sake.
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private void hideBackButton() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void showTopStories() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, TopStoriesFragment.newInstance()).commit();
    }

    public void showComments() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, CommentsFragment.newInstance(null)).commit();
    }

    public void showReplies() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, RepliesFragment.newInstance(null)).commit();
    }


}
