package com.wilson.hackernews.mvp;

import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.model.HackerNewsStory;

import java.util.List;

/**
 * Interface contract for MVP
 */
public interface GetHackerNewsContract {

    interface StoriesView {

        void clearStories();

        void onFetchStoriesSuccess(List<HackerNewsStory> hackerNewsStoryList);

        void onFecthStoriesError();

        void showLoadMore();

        void hideLoadMore();
    }

    interface StoriesPresenter {

        void loadTopStoriesID();

        void loadMoreStories();

        void onDestroy();
    }

    interface CommentsView {

        void clearStories();

        void onFetchCommentsSuccess(List<HackerNewsComment> hackerNewsCommentList);

        void onFecthStoriesError();

        void showLoadMore();

        void hideLoadMore();
    }

    interface CommentsPresenter {

        void loadComments(String[] commentsID);

        void loadMoreComments();

    }

}
