package com.wilson.hackernews.mvp;

import com.wilson.hackernews.fragment.TopStoriesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class ,HackerNewsPresenterModule.class, HackerNewsAPIModule.class})//, HackerNewsViewModule.class})
public interface AppComponent {
    void inject(TopStoriesFragment topStoriesFragment);
}