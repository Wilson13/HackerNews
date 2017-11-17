package com.wilson.hackernews.mvp;

import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.model.HackerNewsStory;

import java.util.List;

/**
 * Interface contract for MVP
 */
public interface GetHackerNewsContract {

    interface StoriesView {

        void onFetchStoriesStart();

        void onFetchStoriesSuccess(List<HackerNewsStory> hackerNewsStoryList);

        void onFecthStoriesError();

        void showLoadMore();

        void hideLoadMore();
    }

    interface StoriesPresenter {

        void loadNewStories();

        void loadMoreStories();

    }

    interface CommentsView {

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
