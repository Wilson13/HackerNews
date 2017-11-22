package com.wilson.hackernews.mvp;

import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.model.HackerNewsStory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class HackerNewsModel implements GetHackerNewsContract.APIModel {

    @Inject
    GetHackerNewsContract.APIModel apiService;

    public HackerNewsModel(GetHackerNewsContract.APIModel apiService) {
        this.apiService = apiService;
    }

    @Override
    public Single<String[]> getTopStories() {

        return this.apiService.getTopStories();
    }

    @Override
    public Observable<HackerNewsStory> getStory(String id) {
        return this.apiService.getStory(id);
    }

    @Override
    public Observable<HackerNewsComment> getComment(String id) {
        return this.apiService.getComment(id);
    }

    @Override
    public Single<List<HackerNewsStory>> getStories(List<String> storiesToPullList) {
        // Use flatMap to ensure the order of items received
        // is same as the list (synchronized).

        // This makes the API call visibly slow. There could
        // be better way but stick with this method for now.
        return Observable.fromIterable(storiesToPullList)
                .flatMap(this::getStory).toList();
    }


    public Single<List<HackerNewsComment>> getComments(List<String> commentsToPullList) {
        // Use flatMap to ensure the order of items received
        // is same as the list (synchronized).

        // This makes the API call visibly slow. There could
        // be better way but stick with this method for now.
        return Observable.fromIterable(commentsToPullList)
                .flatMap(id -> {
                    //Log.d(TAG, "apply: " + id);
                    return apiService.getComment(id);
                }).toList();
    }

}
