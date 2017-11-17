package com.wilson.hackernews.mvp;

import com.wilson.hackernews.model.HackerNewsStory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.wilson.hackernews.other.Constants.NUMBER_OF_STORIES_TO_DISPLAY;

/**
 * StoriesPresenter for MVP
 */

public class HNStoriesPresenter<V> implements GetHackerNewsContract.StoriesPresenter {

    private static final String TAG = "HNStoriesPresenter";

    private GetHackerNewsContract.StoriesView view;
    private HackerNewsAPIDataSource dataSource;
    private String[] topStoriesID;
    private int numStoriesLoaded = 0; // range [0-topStoriesID.length)

    public HNStoriesPresenter(HackerNewsAPIDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setView(V view){
        this.view = (GetHackerNewsContract.StoriesView) view;
        this.loadNewStories();
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
                        loadMoreStories();
                        //pullStories(storiesToPullList);
                    },
                    error -> view.onFecthStoriesError());
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
            //.subscribeOn(Schedulers.io())
            //.observeOn(AndroidSchedulers.mainThread())
            .subscribe(hackerNewsStories -> storiesLoadedHandler(hackerNewsStories),
                    error -> view.onFecthStoriesError()
            );
    }

    private void storiesLoadedHandler(List<HackerNewsStory> hackerNewsStories) {

        view.onFetchStoriesSuccess(hackerNewsStories);
        if (numStoriesLoaded < topStoriesID.length)
            view.showLoadMore();
        else
            view.hideLoadMore();
    }
}
