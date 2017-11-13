package com.wilson.hackernews.mvp;

import android.util.Log;

import com.wilson.hackernews.model.HackerNewsComment;
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

public class HNCommentsPresenter<V> implements GetHackerNewsContract.CommentsPresenter {

    private static final String TAG = "HNCommentsPresenter";

    private V view;
    private HackerNewsAPI apiService;
    private CompositeDisposable disposables;

    private HackerNewsComment hackerNewsComment;
    private List<HackerNewsStory> hackerNewsStoryList;
    private String[] topStoriesID;

    // range [0-topStoriesID.length)
    private int numStoriesLoaded = 0;

    public HNCommentsPresenter(HackerNewsAPI apiService) {
        this.apiService = apiService;
        disposables = new CompositeDisposable();
        hackerNewsStoryList = new ArrayList<>();
    }

    public void setView(V view){
        this.view = view;
    }

    @Override
    public void loadComments(String[] commentsID) {
//        apiService.getItem(storyID)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this.commentHandler, this.singleErrorHandler);
    }

    @Override
    public void loadComment() {

    }

    // Function to load individual story item
    private void loadStories() {
        // Remove previous stories
        hackerNewsStoryList.clear();
        ((GetHackerNewsContract.StoriesView) view).clearStories();

        // Counter to limit loading only 20 stories
        int currentLoaded = 0;
        int start = numStoriesLoaded;
        List<String> storiesToPullList = new ArrayList<>();

        while (numStoriesLoaded < topStoriesID.length) {
            // Get story item from server.
            storiesToPullList.add(topStoriesID[numStoriesLoaded]);
            numStoriesLoaded++;
            currentLoaded++;
            // Stop loading when 20 stories were loaded
            if (currentLoaded == NUMBER_OF_STORIES_TO_DISPLAY) {
                break;
            }
        }

        //String[] storiesToPull = new String[NUMBER_OF_STORIES_TO_DISPLAY];
        //System.arraycopy(topStoriesID, start, storiesToPull, 0, numStoriesLoaded - start);
        //List<String> storiesToPullList = new ArrayList<>(Arrays.asList(storiesToPull));

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
                .subscribe(HNCommentsPresenter.this.loadStoriesHandler, HNCommentsPresenter.this.singleErrorHandler);
    }

    private Consumer<HackerNewsComment> commentHandler = new Consumer<HackerNewsComment>() {
        @Override
        public void accept(HackerNewsComment comment) throws Exception {
            hackerNewsComment = comment;
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
            ((GetHackerNewsContract.StoriesView) view).onFetchStoriesSuccess(hackerNewsStoryList);
        }
    };
}
