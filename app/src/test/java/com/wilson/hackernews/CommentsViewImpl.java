package com.wilson.hackernews;

import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.mvp.GetHackerNewsContract;

import java.util.List;

public class CommentsViewImpl implements GetHackerNewsContract.CommentsView {

    @Override
    public void onFetchCommentsSuccess(List<HackerNewsComment> hackerNewsCommentList) {

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
