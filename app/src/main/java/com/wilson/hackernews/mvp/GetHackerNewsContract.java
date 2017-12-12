package com.wilson.hackernews.mvp;

import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.model.HackerNewsStory;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Interface contract for MVP
 */
public interface GetHackerNewsContract {

    interface StoriesView {

        void onFetchTopStoriesIdSuccess();

        void onFetchStoriesStart();

        void onFetchStoriesSuccess(List<HackerNewsStory> hackerNewsStoryList, int numLoaded);

        void onFetchStoriesError();

        void showLoadMore();

        void hideLoadMore();
    }

    interface StoriesPresenter {

        void loadNewStories();

        void loadMoreStories();

    }

    interface CommentsView {

        void onFetchStoriesStart();

        void onFetchCommentsSuccess(List<HackerNewsComment> hackerNewsCommentList, int numLoaded);

        void onFecthStoriesError();

        void showLoadMore();

        void hideLoadMore();
    }

    interface CommentsPresenter {

        void loadComments(String[] commentsID);

        void loadMoreComments();

    }

    interface APIModel {

        @GET("topstories.json")
        Single<String[]> getTopStories();

        @GET("item/{id}.json")
        Observable<HackerNewsStory> getStory(@Path("id") String id);

        @GET("item/{id}.json")
        Observable<HackerNewsComment> getComment(@Path("id") String id);

        Single<List<HackerNewsStory>> getStories(List<String> storiesToPullList);

        Single<List<HackerNewsComment>> getComments(List<String> commentsToPullList);
    }
}
