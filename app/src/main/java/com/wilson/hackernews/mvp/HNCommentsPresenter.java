package com.wilson.hackernews.mvp;

import com.wilson.hackernews.model.HackerNewsComment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.wilson.hackernews.other.Constants.NUMBER_OF_COMMENTS_TO_DISPLAY;

/**
 * CommentsPresenter for MVP
 */

public class HNCommentsPresenter<V> implements GetHackerNewsContract.CommentsPresenter {

    private static final String TAG = "HNCommentsPresenter";

    private GetHackerNewsContract.CommentsView view;
    private HackerNewsModel dataSource;
    private String[] commentsID;

    // range [0-topStoriesID.length)
    private int numCommentsLoaded = 0;

    public HNCommentsPresenter(HackerNewsModel dataSource) {
        this.dataSource = dataSource;
    }

    public void setView(V view){
        this.view = (GetHackerNewsContract.CommentsView) view;
    }

    @Override
    public void loadComments(String[] commentsID) {
        // Remove previous stories
        this.commentsID = commentsID;
        numCommentsLoaded = 0;
        loadMoreComments();
    }

    @Override
    public void loadMoreComments() {
        // Counter to limit total stories to load
        int currentLoaded = 0;
        List<String> commentsToPullList = new ArrayList<>();

        while (numCommentsLoaded < commentsID.length && currentLoaded < NUMBER_OF_COMMENTS_TO_DISPLAY) {
            // Get comment item from server
            commentsToPullList.add(commentsID[numCommentsLoaded]);
            numCommentsLoaded++;
            currentLoaded++;
        }

        dataSource.getComments(commentsToPullList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::commentsLoadedHandler, error -> view.onFecthStoriesError());
    }

    private void commentsLoadedHandler(List<HackerNewsComment> hackerNewsComments) {
        view.onFetchCommentsSuccess(hackerNewsComments);

        if (numCommentsLoaded < commentsID.length)
            view.showLoadMore();
        else
            view.hideLoadMore();
    }
}
