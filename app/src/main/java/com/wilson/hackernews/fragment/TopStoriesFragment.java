package com.wilson.hackernews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wilson.hackernews.R;
import com.wilson.hackernews.adapter.TopStoriesAdapter;
import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.mvp.GetHackerNewsContract;
import com.wilson.hackernews.mvp.HNStoriesPresenter;
import com.wilson.hackernews.other.MyApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopStoriesFragment extends Fragment implements GetHackerNewsContract.StoriesView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, TopStoriesAdapter.CommentsClickedListener {

    private static final String TAG = "TopStoriesFragment";
    private TopStoriesAdapter topStoriesAdapter;
    private List<HackerNewsStory> hackerNewsStoryList = new ArrayList<>();
    private boolean moreStories = false;
    private ShowCommentsListener delegate;

    // Interface to pass show comments event to parent MainActivity
    public interface ShowCommentsListener {
        void showComments(String[] commentsID);
    }
    @Inject
    HNStoriesPresenter presenter;
    @BindView(R.id.srl_top_stories)
    SwipeRefreshLayout topStoriesSRL;
    @BindView(R.id.rv_top_stories)
    RecyclerView topStoriesRV;
    @BindView(R.id.tv_load_more)
    TextView loadMoreTV;
    @BindView(R.id.ll_stories)
    LinearLayout storiesLL;
    @BindView(R.id.ll_empty_stories)
    LinearLayout emptyStoriesLL;

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
        topStoriesSRL.setOnRefreshListener(this);
        topStoriesSRL.setRefreshing(true); // Show refreshing animation

        topStoriesAdapter = new TopStoriesAdapter();
        topStoriesAdapter.setData(hackerNewsStoryList);
        topStoriesAdapter.setDelegate(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        topStoriesRV.setLayoutManager(mLayoutManager);
        topStoriesRV.setAdapter(topStoriesAdapter);
        loadMoreTV.setOnClickListener(this);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            delegate = (ShowCommentsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ShowCommentsListener");
        }
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh()");
        presenter.loadTopStoriesID();
    }

    @Override
    public void onClick(View v) {
        topStoriesSRL.setRefreshing(true);
        topStoriesRV.getLayoutManager().scrollToPosition(0);
        presenter.loadMoreStories();
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
        topStoriesSRL.setRefreshing(false);
        showStoriesLoaded();
    }

    @Override
    public void onFecthStoriesError() {
        topStoriesSRL.setRefreshing(false);
        showStoriesEmpty();
    }

    @Override
    public void showLoadMore() {
        loadMoreTV.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadMore() {
        loadMoreTV.setVisibility(View.GONE);
    }

    @Override
    public void commentsClicked(String[] commentsID) {
        if (delegate != null)
            delegate.showComments(commentsID);
    }

    private void showStoriesEmpty() {
        storiesLL.setVisibility(View.GONE);
        emptyStoriesLL.setVisibility(View.VISIBLE);
    }

    private void showStoriesLoaded() {
        storiesLL.setVisibility(View.VISIBLE);
        emptyStoriesLL.setVisibility(View.GONE);
    }
}
