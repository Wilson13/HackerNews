package com.wilson.hackernews.mvp;

import android.util.Log;

import com.wilson.hackernews.model.HackerNewsComment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.wilson.hackernews.other.Constants.NUMBER_OF_COMMENTS_TO_DISPLAY;

/**
 * CommentsPresenter for MVP
 */

public class HNCommentsPresenter<V> implements GetHackerNewsContract.CommentsPresenter {

    private static final String TAG = "HNCommentsPresenter";

    private GetHackerNewsContract.CommentsView view;
    private HackerNewsAPI apiService;
    private CompositeDisposable disposables;

    private String[] commentsID;
    private List<HackerNewsComment> hackerNewsCommentsList;

    // range [0-topStoriesID.length)
    private int numCommentsLoaded = 0;

    public HNCommentsPresenter(HackerNewsAPI apiService) {
        this.apiService = apiService;
        disposables = new CompositeDisposable();
        hackerNewsCommentsList = new ArrayList<>();
    }

    public void setView(V view){
        this.view = (GetHackerNewsContract.CommentsView) view;
    }

    @Override
    public void loadComments(String[] commentsID) {
        // Remove previous stories
        this.commentsID = commentsID;
        numCommentsLoaded = 0;
        pullComments();
    }

    @Override
    public void loadMoreComments() {
        pullComments();
    }

    private void pullComments() {
        hackerNewsCommentsList.clear();

        // Counter to limit total stories to load
        int currentLoaded = 0;
        int start = numCommentsLoaded;
        List<String> commentsToPullList = new ArrayList<>();

        while (numCommentsLoaded < commentsID.length && currentLoaded < NUMBER_OF_COMMENTS_TO_DISPLAY) {
            // Get comment item from server
            commentsToPullList.add(commentsID[numCommentsLoaded]);
            numCommentsLoaded++;
            currentLoaded++;
        }

        // Use flatMap to ensure the order of items received
        // is same as the list (synchronized).

        // This makes the API call visibly slow. There could
        // be better way but stick with this method for now.
        Single<List<HackerNewsComment>> commentsObservable = Observable.fromIterable(commentsToPullList)
                .flatMap(id -> {
                    //Log.d(TAG, "apply: " + id);
                    return apiService.getComment(id);
                }).toList();

        commentsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(HNCommentsPresenter.this.loadCommentsHandler, error -> view.onFecthStoriesError());
    }

    private Consumer<List<HackerNewsComment>> loadCommentsHandler = new Consumer<List<HackerNewsComment>>() {
        @Override
        public void accept(List<HackerNewsComment> hackerNewsComments) throws Exception {
            hackerNewsCommentsList.clear();
            hackerNewsCommentsList.addAll(hackerNewsComments);

            if (hackerNewsCommentsList.size() > 0)
                view.onFetchCommentsSuccess(hackerNewsCommentsList);
            else
                view.onFecthStoriesError();

            Log.d(TAG, "numCommentsLoaded: " + numCommentsLoaded + " commentsID.length: " + commentsID.length);
            if (numCommentsLoaded < commentsID.length)
                view.showLoadMore();
            else
                view.hideLoadMore();
        }
    };
}
