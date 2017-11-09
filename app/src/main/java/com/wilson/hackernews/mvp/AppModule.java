package com.wilson.hackernews.mvp;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

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

//    @Provides
//    @Singleton
//    public SharedPreferences providePreferences() {
//        return application.getSharedPreferences(DATA_STORE,
//                Context.MODE_PRIVATE);
//    }

}