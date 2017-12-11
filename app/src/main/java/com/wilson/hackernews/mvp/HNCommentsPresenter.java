package com.wilson.hackernews.mvp;

import android.os.Parcel;
import android.os.Parcelable;

import com.wilson.hackernews.model.HackerNewsComment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.wilson.hackernews.other.Constants.NUMBER_OF_COMMENTS_TO_DISPLAY;

/**
 * CommentsPresenter for MVP
 */

public class HNCommentsPresenter<V> implements GetHackerNewsContract.CommentsPresenter, Parcelable {

    private static final String TAG = "HNCommentsPresenter";

    private GetHackerNewsContract.CommentsView view;
    private HackerNewsModel dataSource;
    private String[] commentsID = new String[] {};

    // range [0-topStoriesID.length)
    private int numCommentsLoaded = 0;

    public HNCommentsPresenter(HackerNewsModel dataSource) {
        this.dataSource = dataSource;
    }

    protected HNCommentsPresenter(Parcel in) {
        commentsID = in.createStringArray();
        numCommentsLoaded = in.readInt();
    }

    public static final Creator<HNCommentsPresenter> CREATOR = new Creator<HNCommentsPresenter>() {
        @Override
        public HNCommentsPresenter createFromParcel(Parcel in) {
            return new HNCommentsPresenter(in);
        }

        @Override
        public HNCommentsPresenter[] newArray(int size) {
            return new HNCommentsPresenter[size];
        }
    };

    public void setView(V view){
        this.view = (GetHackerNewsContract.CommentsView) view;
    }

    public void checkHasMoreStories() {
        if (numCommentsLoaded < commentsID.length)
            view.showLoadMore();
        else
            view.hideLoadMore();
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
        view.onFetchStoriesStart();

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
        checkHasMoreStories();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(commentsID);
        dest.writeInt(numCommentsLoaded);
    }
}
