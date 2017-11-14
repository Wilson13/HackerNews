package com.wilson.hackernews.mvp;

import android.util.Log;

import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.other.HackerNewsAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.wilson.hackernews.other.Constants.NUMBER_OF_STORIES_TO_DISPLAY;

/**
 * StoriesPresenter for MVP
 */

public class HNStoriesPresenter<V> implements GetHackerNewsContract.StoriesPresenter {

    private static final String TAG = "HNStoriesPresenter";

    private V view;
    private HackerNewsAPI apiService;
    private CompositeDisposable disposables;

    private List<HackerNewsStory> hackerNewsStoryList;
    private String[] topStoriesID;

    // range [0-topStoriesID.length)
    private int numStoriesLoaded = 0;

    public HNStoriesPresenter(HackerNewsAPI apiService) {
        this.apiService = apiService;
        disposables = new CompositeDisposable();
        hackerNewsStoryList = new ArrayList<>();
    }

    public void setView(V view){
        this.view = view;
    }

    @Override
    public void loadTopStoriesID() {
        // Reset this variable to load stories from start (first page)
        numStoriesLoaded = 0;
        hackerNewsStoryList.clear();

        disposables.add(
            apiService.getTopStories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this.topStoriesIdHandler, this.singleErrorHandler)
        );
    }

    @Override
    public void loadMoreStories() {
        pullStories();
    }

    @Override
    public void onDestroy() {
    }

    // Function to load NUMBER_OF_STORIES_TO_DISPLAY individual story item
    private void pullStories() {
        // Remove previous stories
        hackerNewsStoryList.clear();
        //((GetHackerNewsContract.StoriesView) view).clearStories();

        // Counter to limit total stories to load
        int currentLoaded = 0;
        int start = numStoriesLoaded;
        List<String> storiesToPullList = new ArrayList<>();

        while (numStoriesLoaded < topStoriesID.length && currentLoaded < NUMBER_OF_STORIES_TO_DISPLAY) {
            // Get story item from server
            storiesToPullList.add(topStoriesID[numStoriesLoaded]);
            numStoriesLoaded++;
            currentLoaded++;
        }

        // Use flatMap to ensure the order of items received
        // is same as the list (synchronized).

        // This makes the API call visibly slow. There could
        // be better way but stick with this method for now.
        Single<List<HackerNewsStory>> storiesObservable = Observable.fromIterable(storiesToPullList)
                .flatMap(new Function<String, Observable<HackerNewsStory>>() {
                    @Override
                    public Observable<HackerNewsStory> apply(String id) throws Exception {
                        Log.d(TAG, "apply: " + id);
                        return apiService.getStory(id);
                    }
                }).toList();

        storiesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(HNStoriesPresenter.this.loadStoriesHandler, HNStoriesPresenter.this.singleErrorHandler);
    }

    private Consumer<String[]> topStoriesIdHandler = new Consumer<String[]>() {
        @Override
        public void accept(String[] topStories) throws Exception {
            topStoriesID = topStories;
            pullStories();
        }
    };

    private Consumer<Throwable> singleErrorHandler = new Consumer<Throwable>() {
        @Override
        public void accept(@NonNull Throwable throwable) throws Exception {
            Log.d(TAG, "error: " + throwable.getLocalizedMessage());
            ((GetHackerNewsContract.StoriesView) view).onFecthStoriesError();
        }
    };

    private Consumer<List<HackerNewsStory>> loadStoriesHandler = new Consumer<List<HackerNewsStory>>() {
        @Override
        public void accept(List<HackerNewsStory> hackerNewsStories) throws Exception {
            hackerNewsStoryList.clear();
            hackerNewsStoryList.addAll(hackerNewsStories);

            if (hackerNewsStoryList.size() > 0)
                ((GetHackerNewsContract.StoriesView) view).onFetchStoriesSuccess(hackerNewsStoryList);
            else
                ((GetHackerNewsContract.StoriesView) view).onFecthStoriesError();

            if (numStoriesLoaded < topStoriesID.length)
                ((GetHackerNewsContract.StoriesView) view).showLoadMore();
            else
                ((GetHackerNewsContract.StoriesView) view).hideLoadMore();
        }
    };
}
