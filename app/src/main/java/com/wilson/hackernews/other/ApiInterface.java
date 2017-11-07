package com.wilson.hackernews.other;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("topstories.json")
    //@GET("citiesJSON")
    //Single<List<TopStory>> getTopStories();
    Single<String[]> getTopStories();

    @GET("topstories.json")
    Call<String[]> getValues();
}
