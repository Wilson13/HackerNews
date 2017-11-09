package com.wilson.hackernews.other;

import com.wilson.hackernews.model.HackerNewsStory;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HackerNewsAPI {

    @GET("topstories.json")
    Single<String[]> getTopStories();

    @GET("item/{id}.json")
    Observable<HackerNewsStory> getStory(@Path("id") String id);
}
