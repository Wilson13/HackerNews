package com.wilson.hackernews;

import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.mvp.GetHackerNewsContract;

import java.util.List;

public class StoriesViewImpl implements GetHackerNewsContract.StoriesView {
    @Override
    public void onFetchStoriesStart() {

    }

    @Override
    public void onFetchStoriesSuccess(List<HackerNewsStory> hackerNewsStoryList) {

    }

    @Override
    public void onFecthStoriesError() {

    }

    @Override
    public void showLoadMore() {

    }

    @Override
    public void hideLoadMore() {

    }
}
