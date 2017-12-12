package com.wilson.hackernews.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import static com.wilson.hackernews.other.Constants.TOP_STORIES_FRAGMENT_PRESENTER_KEY;
import static com.wilson.hackernews.other.Constants.TOP_STORIES_FRAGMENT_STORY_LIST_KEY;

public class TopStoriesFragment extends Fragment implements GetHackerNewsContract.StoriesView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, TopStoriesAdapter.CommentsClickedListener {

    private static final String TAG = "TopStoriesFragment";
    private TopStoriesAdapter topStoriesAdapter;
    private List<HackerNewsStory> hackerNewsStoryList = new ArrayList<>();
    private TopStoriesListener delegate;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean moreStories = false;

    // Interface to pass show comments event to parent MainActivity
    public interface TopStoriesListener {
        void showComments(String[] commentsID);
    }

    @Inject
    public HNStoriesPresenter presenter;

    @BindView(R.id.srl_top_stories) SwipeRefreshLayout topStoriesSRL;
    @BindView(R.id.rv_top_stories) RecyclerView topStoriesRV;
    @BindView(R.id.tv_load_more_stories) TextView loadMoreTV;
    @BindView(R.id.ll_stories) LinearLayout storiesLL;
    @BindView(R.id.ll_empty_stories) LinearLayout emptyStoriesLL;

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

        MyApp.getAppComponent().inject(this);

        if (savedInstanceState != null) {
            hackerNewsStoryList.addAll(savedInstanceState.getParcelableArrayList(TOP_STORIES_FRAGMENT_STORY_LIST_KEY));
            HNStoriesPresenter mPresenter = savedInstanceState.getParcelable(TOP_STORIES_FRAGMENT_PRESENTER_KEY);

            if (mPresenter != null)
                presenter = mPresenter;

            presenter.setView(this);
            presenter.checkHasMoreStories();
        }
        else {
            presenter.setView(this);
            presenter.loadNewStories();
        }

        topStoriesSRL.setOnRefreshListener(this);

        topStoriesAdapter = new TopStoriesAdapter(this);
        topStoriesAdapter.setData(hackerNewsStoryList);

        mLayoutManager = new LinearLayoutManager(getContext());
        topStoriesRV.setLayoutManager(mLayoutManager);
        topStoriesRV.setAdapter(topStoriesAdapter);
        loadMoreTV.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Use bundle to save stories list and presenter.
        // Should be updated to use the new arch component (viewmodel, livedata, etc.) in the future.
        outState.putParcelableArrayList(TOP_STORIES_FRAGMENT_STORY_LIST_KEY, (ArrayList<? extends Parcelable>) hackerNewsStoryList);
        outState.putParcelable(TOP_STORIES_FRAGMENT_PRESENTER_KEY, presenter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            delegate = (TopStoriesListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement TopStoriesListener");
        }
    }

    @Override
    public void onRefresh() {
        hackerNewsStoryList.clear();
        topStoriesAdapter.notifyDataSetChanged();
        hideLoadMore();
        presenter.loadNewStories();
    }

    @Override
    public void onClick(View v) {
        topStoriesSRL.setRefreshing(true);
        hideLoadMore();
        presenter.loadMoreStories();
    }

    @Override
    public void onFetchTopStoriesIdSuccess() {
        hackerNewsStoryList.clear();
    }

    @Override
    public void onFetchStoriesStart() {
        topStoriesSRL.setRefreshing(true); // Show refreshing animation
    }

    @Override
    public void onFetchStoriesSuccess(List<HackerNewsStory> hackerNewsStoryList, int numLoaded) {
        this.hackerNewsStoryList.addAll(hackerNewsStoryList);
        topStoriesAdapter.notifyDataSetChanged();
        topStoriesSRL.setRefreshing(false);
        ((LinearLayoutManager)topStoriesRV.getLayoutManager()).scrollToPositionWithOffset(topStoriesAdapter.getItemCount() - numLoaded, 0);
        showStoriesLoaded();
    }

    @Override
    public void onFetchStoriesError() {
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
    public void storyClicked(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        // Prevent crashes from implicit intent
        if (i.resolveActivity(getActivity().getPackageManager()) != null)
            getActivity().startActivity(i);
    }

    @Override
    public void commentsClicked(String[] commentsID) {
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
