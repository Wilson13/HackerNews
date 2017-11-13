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
import com.wilson.hackernews.adapter.CommentsAdapter;
import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.mvp.GetHackerNewsContract;
import com.wilson.hackernews.mvp.HNStoriesPresenter;
import com.wilson.hackernews.other.MyApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wilson.hackernews.other.Constants.COMMENTS_FRAGMENT_ARGUMENT_KEY;

public class CommentsFragment extends Fragment implements GetHackerNewsContract.StoriesView {

    private static final String TAG = "CommentsFragment";
    private HackerNewsStory story;
    private CommentsAdapter commentsAdapter;
    private List<HackerNewsStory> hackerNewsStoryList = new ArrayList<>();

    @Inject
    HNStoriesPresenter presenter;
    @BindView(R.id.rv_comments) RecyclerView commentsRV;

    public static CommentsFragment newInstance(String[] commentsID)
    {
        CommentsFragment f = new CommentsFragment();
        Bundle args = new Bundle();
        args.putStringArray(COMMENTS_FRAGMENT_ARGUMENT_KEY, commentsID);
        //args.putParcelable(COMMENTS_FRAGMENT_ARGUMENT_KEY, story);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comments, container, false);
        ButterKnife.bind(this, v);
        Bundle args = getArguments();
        story = args.getParcelable(COMMENTS_FRAGMENT_ARGUMENT_KEY);
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

        //commentsAdapter = new CommentsAdapter();
        //commentsAdapter.setData(hackerNewsStoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        commentsRV.setLayoutManager(mLayoutManager);
        commentsRV.setAdapter(commentsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void clearStories() {
        hackerNewsStoryList.clear();
    }

    @Override
    public void onFetchStoriesSuccess(List<HackerNewsStory> hackerNewsStoryList) {
        Log.d(TAG, "onFetchStoriesSuccess: " + hackerNewsStoryList.size());
        hackerNewsStoryList.clear();
        this.hackerNewsStoryList.addAll(hackerNewsStoryList);
        commentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFecthStoriesError() {

    }

    @Override
    public void showLoadMore() {

    }

    @Override
    public void hideLoadMore() {

    }
}
