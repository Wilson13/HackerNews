package com.wilson.hackernews.mvp;

import com.wilson.hackernews.other.HackerNewsAPI;

import dagger.Module;
import dagger.Provides;

@Module
class HackerNewsPresenterModule {

    @Provides
    HackerNewsPresenter providesPresenter(HackerNewsAPI apiService) {
        return new HackerNewsPresenter(apiService);
    }

}
