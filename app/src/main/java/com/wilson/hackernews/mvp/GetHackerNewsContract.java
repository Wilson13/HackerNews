package com.wilson.hackernews.mvp;

import com.wilson.hackernews.model.HackerNewsStory;

import java.util.List;

/**
 * Interface contract for MVP
 */
public interface GetHackerNewsContract {

    interface View {

        void clearStories();

        void onFetchStoriesSuccess(List<HackerNewsStory> hackerNewsStoryList);

        void onFecthStoriesError();
    }

    interface Presenter {
        void loadTopStoriesID();

        void loadMoreStoriesID();

        void onDestroy();
    }

}
