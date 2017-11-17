package com.wilson.hackernews.fragment;

import android.os.Bundle;
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
import com.wilson.hackernews.adapter.RepliesAdapter;
import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.mvp.GetHackerNewsContract;
import com.wilson.hackernews.mvp.HNCommentsPresenter;
import com.wilson.hackernews.other.MyApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wilson.hackernews.other.Constants.REPLIES_FRAGMENT_ARGUMENT_KEY;

/**
 * Uses same View (CommentsView), Presenter (HNCommentsPresenter), and
 * Model (HackerNewsComment) as CommentsFragment since they load same item type.
 */

public class RepliesFragment extends Fragment implements GetHackerNewsContract.CommentsView, View.OnClickListener {

    private static final String TAG = "RepliesFragment";
    private RepliesAdapter repliesAdapter;
    private List<HackerNewsComment> hackerNewsRepliesList = new ArrayList<>();
    private String[] repliesID;

    @Inject
    HNCommentsPresenter presenter;

    @BindView(R.id.srl_replies) SwipeRefreshLayout repliesSRL;
    @BindView(R.id.rv_replies) RecyclerView repliesRV;
    @BindView(R.id.ll_replies) LinearLayout repliesLL;
    @BindView(R.id.ll_empty_replies) LinearLayout emptyRepliesLL;
    @BindView(R.id.tv_load_more) TextView loadMoreTV;

    public static RepliesFragment newInstance(String[] repliesID)
    {
        RepliesFragment f = new RepliesFragment();
        Bundle args = new Bundle();
        args.putStringArray(REPLIES_FRAGMENT_ARGUMENT_KEY, repliesID);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_replies, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        repliesID = args.getStringArray(REPLIES_FRAGMENT_ARGUMENT_KEY);

        MyApp.getAppComponent().inject(this);
        presenter.setView(this);
        presenter.loadComments(repliesID);

        repliesSRL.setEnabled(false); // Disable refresh function
        repliesSRL.setRefreshing(true); // Show refreshing animation
        repliesAdapter = new RepliesAdapter();
        repliesAdapter.setComments(hackerNewsRepliesList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        repliesRV.setLayoutManager(mLayoutManager);
        repliesRV.setAdapter(repliesAdapter);
        loadMoreTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        repliesSRL.setRefreshing(true);
        hackerNewsRepliesList.clear();
        repliesAdapter.notifyDataSetChanged();
        hideLoadMore();
        presenter.loadMoreComments();
    }

    @Override
    public void onFetchCommentsSuccess(List<HackerNewsComment> hackerNewsCommentList) {
        this.hackerNewsRepliesList.clear();
        this.hackerNewsRepliesList.addAll(hackerNewsCommentList);
        repliesAdapter.notifyDataSetChanged();
        repliesSRL.setRefreshing(false);
        showStoriesLoaded();
    }

    @Override
    public void onFecthStoriesError() {
        repliesSRL.setRefreshing(false);
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

    private void showStoriesEmpty() {
        repliesLL.setVisibility(View.GONE);
        emptyRepliesLL.setVisibility(View.VISIBLE);
    }

    private void showStoriesLoaded() {
        repliesLL.setVisibility(View.VISIBLE);
        emptyRepliesLL.setVisibility(View.GONE);
    }
}
