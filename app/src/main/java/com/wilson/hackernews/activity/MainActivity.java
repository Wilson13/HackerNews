package com.wilson.hackernews.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.wilson.hackernews.R;
import com.wilson.hackernews.fragment.CommentsFragment;
import com.wilson.hackernews.fragment.TopStoriesFragment;
import com.wilson.hackernews.other.MyApp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TopStoriesFragment.ShowCommentsListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        MyApp.getAppComponent().inject(this);

        // Set up toolbar
        setSupportActionBar(toolbar);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fl_main) != null) {
            // Not restored from previous state
            if (savedInstanceState == null) {
                showTopStories();
            }
        }
    }

    private void showTopStories() {
        // Show maim fragment if not displayed
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_main, TopStoriesFragment.newInstance()).commit();
        hideBackButton();
    }

    private void showComments() {

        showBackButton();
    }

    private void showBackButton() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void hideBackButton() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void showComments(String[] commentsID) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_main, CommentsFragment.newInstance(commentsID)).commit();
    }
}
