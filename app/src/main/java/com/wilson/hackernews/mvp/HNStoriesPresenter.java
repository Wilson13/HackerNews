package com.wilson.hackernews.mvp;

import android.os.Parcel;
import android.os.Parcelable;

import com.wilson.hackernews.model.HackerNewsStory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.wilson.hackernews.other.Constants.NUMBER_OF_STORIES_TO_DISPLAY;

/**
 * StoriesPresenter for MVP
 */

public class HNStoriesPresenter<V> implements GetHackerNewsContract.StoriesPresenter, Parcelable {

    private static final String TAG = "HNStoriesPresenter";

    private GetHackerNewsContract.StoriesView view;
    private HackerNewsModel dataSource;
    private String[] topStoriesID = new String[] {};
    private int numStoriesLoaded = 0; // range [0-topStoriesID.length)

    public HNStoriesPresenter(HackerNewsModel dataSource) {
        this.dataSource = dataSource;
    }

    protected HNStoriesPresenter(Parcel in) {
        topStoriesID = in.createStringArray();
        numStoriesLoaded = in.readInt();
    }

    public static final Creator<HNStoriesPresenter> CREATOR = new Creator<HNStoriesPresenter>() {
        @Override
        public HNStoriesPresenter createFromParcel(Parcel in) {
            return new HNStoriesPresenter(in);
        }

        @Override
        public HNStoriesPresenter[] newArray(int size) {
            return new HNStoriesPresenter[size];
        }
    };

    public void setView(V view){
        this.view = (GetHackerNewsContract.StoriesView) view;
    }

    public void checkHasMoreStories() {
        if (numStoriesLoaded < topStoriesID.length)
            view.showLoadMore();
        else
            view.hideLoadMore();
    }

    @Override
    public void loadNewStories() {
        // Reset this variable to load stories from start (first page)
        numStoriesLoaded = 0;

        dataSource.getTopStories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(storiesID -> {
                        topStoriesID = storiesID;
                        view.onFetchStoriesStart();
                        view.onFetchTopStoriesIdSuccess();
                        loadMoreStories();
                    },
                    error -> view.onFetchStoriesError());
    }

    @Override
    public void loadMoreStories() {
        view.onFetchStoriesStart();
        // Counter to limit total stories to load
        int currentLoaded = 0;
        List<String> storiesToPullList = new ArrayList<>();

        while (numStoriesLoaded < topStoriesID.length && currentLoaded < NUMBER_OF_STORIES_TO_DISPLAY) {
            // Get story item from server
            storiesToPullList.add(topStoriesID[numStoriesLoaded]);
            numStoriesLoaded++;
            currentLoaded++;
        }

        // Load NUMBER_OF_STORIES_TO_DISPLAY individual story item
        dataSource.getStories(storiesToPullList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(hackerNewsStories -> storiesLoadedHandler(hackerNewsStories),
                    error -> view.onFetchStoriesError()
            );
    }

    private void storiesLoadedHandler(List<HackerNewsStory> hackerNewsStories) {
        view.onFetchStoriesSuccess(hackerNewsStories);
        checkHasMoreStories();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(topStoriesID);
        dest.writeInt(numStoriesLoaded);
    }
}
