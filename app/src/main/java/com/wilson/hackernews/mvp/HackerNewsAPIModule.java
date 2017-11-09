package com.wilson.hackernews.mvp;

import com.wilson.hackernews.other.HackerNewsAPI;
import com.wilson.hackernews.other.RetrofitSingleton;

import dagger.Module;
import dagger.Provides;

import static com.wilson.hackernews.other.Constants.HACKER_NEWS_BASE_URL;

@Module
class HackerNewsAPIModule {

    @Provides
    HackerNewsAPI providesHackerNewsAPI() {
            return RetrofitSingleton.getClient(HACKER_NEWS_BASE_URL).create(HackerNewsAPI.class);
    }
}
