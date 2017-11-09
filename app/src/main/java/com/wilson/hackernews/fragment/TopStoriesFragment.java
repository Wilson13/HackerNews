package com.wilson.hackernews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.hackernews.R;
import com.wilson.hackernews.adapter.TopStoriesAdapter;
import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.mvp.GetHackerNewsContract;
import com.wilson.hackernews.mvp.HackerNewsPresenter;
import com.wilson.hackernews.other.MyApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopStoriesFragment extends Fragment implements GetHackerNewsContract.View {

    private static final String TAG = "TopStoriesFragment";
    private TopStoriesAdapter topStoriesAdapter;
    private List<HackerNewsStory> hackerNewsStoryList = new ArrayList<>();

    @Inject
    HackerNewsPresenter presenter;
    @BindView(R.id.rv_top_stories) RecyclerView topStoriesRV;

    public static TopStoriesFragment newInstance()
    {
        return new TopStoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_stories, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }

        MyApp.getAppComponent().inject(this);
        presenter.setView(this);
        presenter.loadTopStoriesID();

        topStoriesAdapter = new TopStoriesAdapter(hackerNewsStoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        topStoriesRV.setLayoutManager(mLayoutManager);
        topStoriesRV.setAdapter(topStoriesAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        //hackerNewsStoryList.clear();
//        hackerNewsStoryList.addAll(DBHelper.getAllCountries());
//        if (topStoriesRV.getAdapter() == null) {
//            topStoriesAdapter = new TopStoriesAdapter(hackerNewsStoryList);
//        }
//        TopStoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearStories() {
        hackerNewsStoryList.clear();
    }

    @Override
    public void onFetchStoriesSuccess(List<HackerNewsStory> hackerNewsStoryList) {
        Log.d(TAG, "onFetchStoriesSuccess: " + hackerNewsStoryList.size());
        this.hackerNewsStoryList.clear();
        this.hackerNewsStoryList.addAll(hackerNewsStoryList);
        topStoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFecthStoriesError() {

    }
}
