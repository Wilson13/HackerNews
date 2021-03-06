package com.wilson.hackernews.fragment;

import android.content.Context;
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
import com.wilson.hackernews.adapter.CommentsAdapter;
import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.mvp.GetHackerNewsContract;
import com.wilson.hackernews.mvp.HNCommentsPresenter;
import com.wilson.hackernews.other.MyApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wilson.hackernews.other.Constants.COMMENTS_FRAGMENT_COMMENTS_ID_KEY;
import static com.wilson.hackernews.other.Constants.COMMENTS_FRAGMENT_COMMENT_LIST_KEY;
import static com.wilson.hackernews.other.Constants.COMMENTS_FRAGMENT_PRESENTER_KEY;

public class CommentsFragment extends Fragment implements GetHackerNewsContract.CommentsView, View.OnClickListener, CommentsAdapter.RepliesClickedListener {

    private static final String TAG = "CommentsFragment";
    private CommentsAdapter commentsAdapter;
    private List<HackerNewsComment> hackerNewsCommentList = new ArrayList<>();
    private CommentsListener delegate;
    private String[] commentsID;

    public interface CommentsListener{
        void showReplies(String[] repliesID);
    }

    @Inject
    HNCommentsPresenter presenter;

    @BindView(R.id.srl_comments) SwipeRefreshLayout commentsSRL;
    @BindView(R.id.rv_comments) RecyclerView commentsRV;
    @BindView(R.id.ll_comments) LinearLayout commentsLL;
    @BindView(R.id.ll_empty_comments) LinearLayout emptyCommentsLL;
    @BindView(R.id.tv_load_more_comments) TextView loadMoreTV;

    public static CommentsFragment newInstance(String[] commentsID)
    {
        CommentsFragment f = new CommentsFragment();
        Bundle args = new Bundle();
        args.putStringArray(COMMENTS_FRAGMENT_COMMENTS_ID_KEY, commentsID);
        //args.putParcelable(COMMENTS_FRAGMENT_COMMENTS_ID_KEY, story);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comments, container, false);
        ButterKnife.bind(this, v);

        Bundle args = getArguments();
        commentsID = args.getStringArray(COMMENTS_FRAGMENT_COMMENTS_ID_KEY);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        commentsID = args.getStringArray(COMMENTS_FRAGMENT_COMMENTS_ID_KEY);

        MyApp.getAppComponent().inject(this);

        // Disable refresh function, put before any other code to make sure setRefreshing works when called.
        commentsSRL.setEnabled(false);

        if (savedInstanceState != null) {
            hackerNewsCommentList.addAll(savedInstanceState.getParcelableArrayList(COMMENTS_FRAGMENT_COMMENT_LIST_KEY));
            HNCommentsPresenter mPresenter = savedInstanceState.getParcelable(COMMENTS_FRAGMENT_PRESENTER_KEY);

            if (mPresenter != null)
                presenter = mPresenter;

            presenter.setView(this);
            presenter.checkHasMoreStories();
        }
        else {
            presenter.setView(this);
            presenter.loadComments(commentsID); // Pull comments from server
        }

        commentsAdapter = new CommentsAdapter(this);
        commentsAdapter.setComments(hackerNewsCommentList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        commentsRV.setLayoutManager(mLayoutManager);
        commentsRV.setAdapter(commentsAdapter);
        loadMoreTV.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Use bundle to save stories list and presenter.
        // Should be updated to use the new arch component (viewmodel, livedata, etc.) in the future.
        outState.putParcelableArrayList(COMMENTS_FRAGMENT_COMMENT_LIST_KEY, (ArrayList<? extends Parcelable>) hackerNewsCommentList);
        outState.putParcelable(COMMENTS_FRAGMENT_PRESENTER_KEY, presenter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            delegate = (CommentsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CommentsListener");
        }
    }

    @Override
    public void onClick(View v) {
        commentsSRL.setRefreshing(true);
        hideLoadMore();
        presenter.loadMoreComments();
    }

    @Override
    public void onFetchStoriesStart() {
        commentsSRL.setRefreshing(true); // Show refreshing animation
    }

    @Override
    public void onFetchCommentsSuccess(List<HackerNewsComment> hackerNewsCommentList, int numLoaded) {
        this.hackerNewsCommentList.addAll(hackerNewsCommentList);
        commentsAdapter.notifyDataSetChanged();
        commentsSRL.setRefreshing(false);
        ((LinearLayoutManager)commentsRV.getLayoutManager()).scrollToPositionWithOffset(commentsAdapter.getItemCount() - numLoaded, 0);
        showStoriesLoaded();
    }

    @Override
    public void onFecthStoriesError() {
        commentsSRL.setRefreshing(false);
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
    public void repliesClicked(String[] repliesID) {
        //if (delegate != null) // Removed for code coverage, not possible to encounter null too.
        delegate.showReplies(repliesID);
    }

    private void showStoriesEmpty() {
        commentsLL.setVisibility(View.GONE);
        emptyCommentsLL.setVisibility(View.VISIBLE);
    }

    private void showStoriesLoaded() {
        commentsLL.setVisibility(View.VISIBLE);
        emptyCommentsLL.setVisibility(View.GONE);
    }
}
