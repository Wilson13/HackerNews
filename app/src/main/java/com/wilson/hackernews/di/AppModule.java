package com.wilson.hackernews.di;

import android.app.Application;

import com.wilson.hackernews.mvp.HackerNewsModel;
import com.wilson.hackernews.mvp.GetHackerNewsContract;
import com.wilson.hackernews.mvp.HNCommentsPresenter;
import com.wilson.hackernews.mvp.HNStoriesPresenter;
import com.wilson.hackernews.other.RetrofitSingleton;

import dagger.Module;
import dagger.Provides;

import static com.wilson.hackernews.other.Constants.HACKER_NEWS_BASE_URL;

@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    Application providesApplication() {
        return application;
    }

    @Provides
    HNStoriesPresenter providesPresenter(HackerNewsModel dataSource) {
        return new HNStoriesPresenter(dataSource);
    }

    @Provides
    HNCommentsPresenter providesCommentsPresenter(HackerNewsModel dataSource) {
        return new HNCommentsPresenter(dataSource);
    }

    @Provides
    GetHackerNewsContract.APIModel providesHackerNewsAPI() {
        return RetrofitSingleton.getClient(HACKER_NEWS_BASE_URL).create(GetHackerNewsContract.APIModel.class);
    }

    @Provides
    HackerNewsModel providesHackerNewsAPIDataSource(GetHackerNewsContract.APIModel apiService) {
        return new HackerNewsModel(apiService);
    }

//    @Binds
//    abstract GetHackerNewsContract.StoriesView providesView(TopStoriesFragment topStoriesFragment);
//    @Provides
//    @Singleton
//    public SharedPreferences providePreferences() {
//        return application.getSharedPreferences(DATA_STORE,
//                Context.MODE_PRIVATE);
//    }

}