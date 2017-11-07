package com.wilson.hackernews.mvp;

import android.util.Log;

import com.wilson.hackernews.other.ApiInterface;
import com.wilson.hackernews.other.RetrofitSingleton;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.wilson.hackernews.other.Constants.HACKER_NEWS_BASE_URL;

/**
 * Presenter for MVP
 */

public class HackerNewsPresenter implements GetHackerNewsContract.Presenter {

    private static final String TAG = "HackerNewsPresenter";

    private ApiInterface apiService;
    private CompositeDisposable disposables = new CompositeDisposable();

    public HackerNewsPresenter() {
        apiService = RetrofitSingleton.getClient(HACKER_NEWS_BASE_URL).create(ApiInterface.class);
    }

    @Override
    public void loadTopStories() {
        Log.d(this.getClass().getSimpleName(), "loadTopStories");
        disposables.add(apiService.getTopStories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.responseHandler, this.errorHandler)
        );

//        apiService.getTopStories()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new SingleObserver<List<TopStory>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.d(TAG, "onSubscribe");
//                    }
//
//                    @Override
//                    public void onSuccess(List<TopStory> topStories) {
//                        Log.d(TAG, "onSuccess");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, "onError: " + e.toString());
//                    }
//                });

//        apiService.getTopStories()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new SingleObserver<String[]>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.d(TAG, "onSubscribe");
//                    }
//
//                    @Override
//                    public void onSuccess(String[] strings) {
//                        for (String result : strings) {
//                            Log.d(TAG, "onSuccess: " + result);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, "onError: " + e.toString());
//                    }
//                });

//        apiService.getValues().enqueue(new Callback<String[]>() {
//            @Override
//            public void onResponse(Call<String[]> call, Response<String[]> response) {
//                for (String respond : response.body()) {
//                    Log.i(TAG, "onResponse " + respond);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String[]> call, Throwable t) {
//                Log.e(TAG, "onFailure " + t.getMessage());
//            }
//        });
    }

    private Consumer<String[]> responseHandler = new Consumer<String[]>() {
        @Override
        public void accept(String[] strings) throws Exception {
            for (String results : strings) {
                Log.d(TAG, "result: " + results);
            }
        }
    };

    private Consumer<Throwable> errorHandler = new Consumer<Throwable>() {
        @Override
        public void accept(@NonNull Throwable throwable) throws Exception {
            Log.d(TAG, "error: " + throwable.getLocalizedMessage());
        }
    };

    private SingleObserver<String> getSingleObserver() {
        return new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onSuccess(String value) {
                Log.d(TAG, " onNext value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, " onError : " + e.getMessage());
            }
        };
    }

}
