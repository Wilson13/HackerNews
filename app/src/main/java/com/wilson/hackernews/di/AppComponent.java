package com.wilson.hackernews.di;

import com.wilson.hackernews.activity.MainActivity;
import com.wilson.hackernews.fragment.CommentsFragment;
import com.wilson.hackernews.fragment.RepliesFragment;
import com.wilson.hackernews.fragment.TopStoriesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
    void inject(TopStoriesFragment topStoriesFragment);
    void inject(CommentsFragment commentsFragment);
    void inject(RepliesFragment repliesFragment);
}