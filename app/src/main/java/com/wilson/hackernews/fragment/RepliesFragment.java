package com.wilson.hackernews.fragment;

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
import static com.wilson.hackernews.other.Constants.REPLIES_FRAGMENT_PRESENTER_KEY;
import static com.wilson.hackernews.other.Constants.REPLIES_FRAGMENT_REPLY_LIST_KEY;

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
    @BindView(R.id.tv_load_more_replies) TextView loadMoreTV;

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

        repliesSRL.setEnabled(false); // Disable refresh function

        if (savedInstanceState != null) {
            hackerNewsRepliesList.addAll(savedInstanceState.getParcelableArrayList(REPLIES_FRAGMENT_REPLY_LIST_KEY));
            HNCommentsPresenter mPresenter = savedInstanceState.getParcelable(REPLIES_FRAGMENT_PRESENTER_KEY);

            if (mPresenter != null)
                presenter = mPresenter;

            presenter.setView(this);
            presenter.checkHasMoreStories();
        }
        else {
            presenter.setView(this);
            presenter.loadComments(repliesID); // Pull comments from server
        }

        repliesAdapter = new RepliesAdapter();
        repliesAdapter.setComments(hackerNewsRepliesList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        repliesRV.setLayoutManager(mLayoutManager);
        repliesRV.setAdapter(repliesAdapter);
        loadMoreTV.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Use bundle to save stories list and presenter.
        // Should be updated to use the new arch component (viewmodel, livedata, etc.) in the future.
        outState.putParcelableArrayList(REPLIES_FRAGMENT_REPLY_LIST_KEY, (ArrayList<? extends Parcelable>) hackerNewsRepliesList);
        outState.putParcelable(REPLIES_FRAGMENT_PRESENTER_KEY, presenter);
    }

    @Override
    public void onClick(View v) {
        repliesSRL.setRefreshing(true);
        hideLoadMore();
        presenter.loadMoreComments();
    }

    @Override
    public void onFetchStoriesStart() {
        repliesSRL.setRefreshing(true); // Show refreshing animation
    }

    @Override
    public void onFetchCommentsSuccess(List<HackerNewsComment> hackerNewsCommentList, int numLoaded) {
        this.hackerNewsRepliesList.addAll(hackerNewsCommentList);
        repliesAdapter.notifyDataSetChanged();
        repliesSRL.setRefreshing(false);
        ((LinearLayoutManager)repliesRV.getLayoutManager()).scrollToPositionWithOffset(repliesAdapter.getItemCount() - numLoaded, 0);
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
