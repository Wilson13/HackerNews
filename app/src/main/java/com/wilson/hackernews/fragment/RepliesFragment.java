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
import com.wilson.hackernews.adapter.CommentsAdapter;
import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.mvp.GetHackerNewsContract;
import com.wilson.hackernews.mvp.HNCommentsPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wilson.hackernews.other.Constants.COMMENTS_FRAGMENT_ARGUMENT_KEY;

public class RepliesFragment extends Fragment implements GetHackerNewsContract.CommentsView, View.OnClickListener {

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
    @BindView(R.id.tv_load_more) TextView loadMoreTV;

    public static RepliesFragment newInstance(String[] commentsID)
    {
        RepliesFragment f = new RepliesFragment();
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
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }
        Bundle args = getArguments();
        //commentsID = args.getParcelable(COMMENTS_FRAGMENT_ARGUMENT_KEY);
        commentsID = args.getStringArray(COMMENTS_FRAGMENT_ARGUMENT_KEY);

        //MyApp.getAppComponent().inject(this);
        presenter.setView(this);
        presenter.loadComments(commentsID);

        commentsSRL.setEnabled(false); // Disable refresh function
        commentsSRL.setRefreshing(true); // Show refreshing animation
        commentsAdapter = new CommentsAdapter();
        commentsAdapter.setComments(hackerNewsCommentList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        commentsRV.setLayoutManager(mLayoutManager);
        commentsRV.setAdapter(commentsAdapter);
        loadMoreTV.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
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
        presenter.loadMoreComments();
    }

    @Override
    public void clearStories() {
        hackerNewsCommentList.clear();
    }

    @Override
    public void onFetchCommentsSuccess(List<HackerNewsComment> hackerNewsCommentList) {
        Log.d(TAG, "onFetchCommentsSuccess: " + hackerNewsCommentList.size());
        this.hackerNewsCommentList.clear();
        this.hackerNewsCommentList.addAll(hackerNewsCommentList);
        commentsAdapter.notifyDataSetChanged();
        commentsSRL.setRefreshing(false);
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
        loadMoreTV.setVisibility(View.INVISIBLE);
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
