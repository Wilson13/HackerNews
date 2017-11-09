package com.wilson.hackernews.mvp;

import com.wilson.hackernews.fragment.TopStoriesFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class HackerNewsViewModule {

    @Binds
    abstract GetHackerNewsContract.View providesView(TopStoriesFragment topStoriesFragment);
}
