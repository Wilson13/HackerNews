package com.wilson.hackernews.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wilson.hackernews.R;
import com.wilson.hackernews.fragment.TopStoriesFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
