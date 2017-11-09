package com.wilson.hackernews.mvp;

import android.util.Log;

import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.other.HackerNewsAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.wilson.hackernews.other.Constants.NUMBER_OF_STORIES_TO_DISPLAY;

/**
 * Presenter for MVP
 */

public class HackerNewsPresenter<V> implements GetHackerNewsContract.Presenter {

    private static final String TAG = "HackerNewsPresenter";

    private V view;
    private HackerNewsAPI apiService;
    private CompositeDisposable disposables;

    private List<HackerNewsStory> hackerNewsStoryList;
    private String[] topStoriesID;

    // range [0-topStoriesID.length)
    private int numStoriesLoaded = 0;

    HackerNewsPresenter(HackerNewsAPI apiService) {
        this.apiService = apiService;
        disposables = new CompositeDisposable();
        hackerNewsStoryList = new ArrayList<>();
    }

    public void setView(V view){
        this.view = view;
    }

    @Override
    public void loadTopStoriesID() {
        disposables.add(apiService.getTopStories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.topStoriesIdHandler, this.singleErrorHandler)
        );
    }

    @Override
    public void loadMoreStoriesID() {

    }

    @Override
    public void onDestroy() {
    }

    private void loadStories() {
        // Remove previous stories
        hackerNewsStoryList.clear();
        ((GetHackerNewsContract.View) view).clearStories();

        // Counter to limit loading only 20 stories
        int currentLoaded = 0;
        int start = numStoriesLoaded;
        while (numStoriesLoaded < topStoriesID.length) {

            //Log.d(TAG,"numStoriesLoaded: " + numStoriesLoaded);
            // Get story item from server.

            // Couldn't use flatMap() from rxjava due to supporting api 11.
            // Messed up all kinds of function calling as many new functions
            // can't be used when importing lower version of libraries.

            // Multiple notifyDataSetChanged called in TopStoriesFragment due
            // to the async nature, can't determine if the last API call is completed.
//            disposables.add(apiService.getStory(topStoriesID[numStoriesLoaded])
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this.loadStoryHandler, this.singleErrorHandler)
//            );
            numStoriesLoaded++;
            currentLoaded++;
            // Stop loading when 20 stories were loaded
            if (currentLoaded == NUMBER_OF_STORIES_TO_DISPLAY) {
                break;
            }
        }

        String[] storiesToPull = new String[NUMBER_OF_STORIES_TO_DISPLAY];
        System.arraycopy(topStoriesID, start, storiesToPull, 0, numStoriesLoaded - start);
        List<String> storiesToPullList = new ArrayList<>(Arrays.asList(storiesToPull));

        // Use flatMap to ensure the order of items received is same as the list
        Observable<HackerNewsStory> storyObservable = Observable.fromIterable(storiesToPullList)
                .flatMap(new Function<String, Observable<HackerNewsStory>>() {
                    @Override
                    public Observable<HackerNewsStory> apply(String id) throws Exception {
                        Log.d(TAG, "apply: " + id);
                        return apiService.getStory(id);
                    }
                });

        storyObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(HackerNewsPresenter.this.loadStoryHandler, HackerNewsPresenter.this.singleErrorHandler);
    }

    private Consumer<String[]> topStoriesIdHandler = new Consumer<String[]>() {
        @Override
        public void accept(String[] topStories) throws Exception {
            //for (String results : topStories) {
                //Log.d(TAG, "result: " + results);
            //}
            topStoriesID = topStories;
            loadStories();
        }
    };

    private Consumer<Throwable> singleErrorHandler = new Consumer<Throwable>() {
        @Override
        public void accept(@NonNull Throwable throwable) throws Exception {
            Log.d(TAG, "error: " + throwable.getLocalizedMessage());
        }
    };

    private Consumer<HackerNewsStory> loadStoryHandler = new Consumer<HackerNewsStory>() {
        @Override
        public void accept(HackerNewsStory story) throws Exception {
            //Log.d(TAG, "story: " + story.getTitle() + "hackerNewsStoryList: " + hackerNewsStoryList.size());
            hackerNewsStoryList.add(story);

//            Collections.sort(hackerNewsStoryList, new Comparator<HackerNewsStory>() {
//                @Override
//                public int compare(HackerNewsStory lhs, HackerNewsStory rhs) {
//                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
//                    return lhs.getTime() < rhs.getTime() ? -1 : (lhs.getTime() > rhs.getTime()) ? 1 : 0;
//                }
//            });

            ((GetHackerNewsContract.View) view).onFetchStoriesSuccess(hackerNewsStoryList);
        }
    };
}
