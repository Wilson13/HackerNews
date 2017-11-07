package com.wilson.hackernews.mvp;

import com.wilson.hackernews.model.TopStory;

import java.util.List;

/**
 * Interface contract for MVP
 */
public interface GetHackerNewsContract {

    interface View {
        void onFetchDataSuccess(List<TopStory> topStories);

        void onFecthDataError();
    }

    interface Presenter {
        void loadTopStories();
    }

}
