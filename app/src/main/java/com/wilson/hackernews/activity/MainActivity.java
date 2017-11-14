package com.wilson.hackernews.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wilson.hackernews.R;
import com.wilson.hackernews.fragment.CommentsFragment;
import com.wilson.hackernews.fragment.TopStoriesFragment;
import com.wilson.hackernews.other.MyApp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TopStoriesFragment.TopStoriesListener, CommentsFragment.CommentsListener {

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

    @Override
    public void onBackPressed() {
        Fragment displayFragment = getSupportFragmentManager().findFragmentById(R.id.fl_main);

        // If it is not an instance of TopStoriesFragment, just pop fragment from stack.
        if (!(displayFragment instanceof TopStoriesFragment)) {
            getSupportFragmentManager().popBackStack();
            getSupportActionBar().setTitle(R.string.title_top_stories);
            hideBackButton();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Make back button in toolbar function properly
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showBackButton() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void hideBackButton() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void showTopStories() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, TopStoriesFragment.newInstance()).commit();
        getSupportActionBar().setTitle(R.string.title_top_stories);
        hideBackButton();
    }

    @Override
    public void showComments(String[] commentsID) {
        // Add fragments to stack
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_main, CommentsFragment.newInstance(commentsID), CommentsFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(CommentsFragment.class.getSimpleName());
        fragmentTransaction.commit();

        getSupportActionBar().setTitle(R.string.title_comments);
        showBackButton();
    }

    @Override
    public void showReplies(String[] repliesID) {

    }
}
