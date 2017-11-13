package com.wilson.hackernews.other;

import android.app.Application;

import com.wilson.hackernews.di.AppComponent;
import com.wilson.hackernews.di.AppModule;
import com.wilson.hackernews.di.DaggerAppComponent;

public class MyApp extends Application {

    protected static MyApp instance;

    public static MyApp get() {
        return instance;
    }

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // init AppComponent on start of the Application
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(instance))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

}
