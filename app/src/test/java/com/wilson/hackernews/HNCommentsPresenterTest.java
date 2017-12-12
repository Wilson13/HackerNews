package com.wilson.hackernews;

import com.google.gson.Gson;
import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.mvp.GetHackerNewsContract;
import com.wilson.hackernews.mvp.HNCommentsPresenter;
import com.wilson.hackernews.mvp.HackerNewsModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HNCommentsPresenterTest {

    @Mock
    private GetHackerNewsContract.CommentsView commentsView;
    @Mock
    private HackerNewsModel dataSource;
    @InjectMocks
    private HNCommentsPresenter commentsPresenter = new HNCommentsPresenter(dataSource);

    private String commentID;
    private String commentID2;
    private String[] commentsID;
    private List<String> commentsToPullList = new ArrayList<>();
    private ArrayList hackerNewsCommentList;

    private HackerNewsComment comment;
    private HackerNewsComment comment2;

    //@Mock
    //HNStoriesPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //RxAndroidPlugins.setInitMainThreadSchedulerHandler(
        //        __ -> Schedulers.trampoline());

        Scheduler immediate = new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);

        commentsPresenter.setView(commentsView);
        commentID = "15702188";
        commentID2 = "15705190";
        commentsID = new String[] {commentID, commentID2};
        commentsToPullList = Arrays.asList(commentsID);

        comment = new Gson().fromJson("{\"by\" : \"iamleppert\", \"id\" : 15702188, \"kids\" : [ 15702419, 15702707, 15702622, 15705397, 15702670 ],\"parent\" : 15701417, \"text\" : \"Anyone who has worked in real web development knows that source maps are barely functional. When they do work, they are slow and there&#x27;s a non-trivial delay, during which time the minified source or transpiled source or whatever is shown.<p>There&#x27;s a 50&#x2F;50 shot they won&#x27;t even work at all, and of that 50% of the time they do work, they often don&#x27;t have the correct line and column.<p>Again, things like the interactive debugger and &quot;pause on caught exceptions&quot; often casually break with source maps.<p>In short, it&#x27;s a mess.\", \"time\" : 1510732926, \"type\" : \"comment\"}", HackerNewsComment.class);
        comment2 = new Gson().fromJson("{\"by\" : \"jschorr\", \"id\" : 15705190, \"kids\" : [ 15706721, 15705436 ], \"parent\" : 15701417, \"text\" : \"Some interesting history of source maps: They actually are older than the article suggests, having been original developed and used internally at Google starting in early 2009. At the time, Google had some of the largest applications written in JavaScript running inside of the browser, optimized and minified using Closure Compiler [1]. One focus of Closure Compiler was&#x2F;is minimizing bytes sent over wire, which resulted in trying to debug issues that occurred on Line 1, Character XXXXX of the minified file. As an engineer working 20% on a Gmail feature, I grew incredibly frustrated with...\",\"time\" : 1510764749,\"type\" : \"comment\"}", HackerNewsComment.class);
        hackerNewsCommentList = new ArrayList<>(Arrays.asList(comment, comment2));
    }

    @Test
    public void HNCommentsPresenterLoadCommentsSuccessTest() {

        // When API getComments(commentsToPullList) is called, return the hackerNewsCommentList defined.
        when(dataSource.getComments(commentsToPullList)).thenReturn(Observable.fromIterable(commentsToPullList).flatMap(id -> dataSource.getComment(id)).toList());
        when(dataSource.getComment(commentID)).thenReturn(Observable.just(comment));
        when(dataSource.getComment(commentID2)).thenReturn(Observable.just(comment2));

        // When setView(View view) is called, verify loadNewStories is called.
        //HNCommentsPresenter spy = Mockito.spy(new HNCommentsPresenter(dataSource));
        //commentsPresenter.setView(commentsView);
        commentsPresenter.loadComments(commentsID);
        verify(dataSource).getComments(commentsToPullList);

        dataSource.getComments(commentsToPullList).subscribe(); // This line is required for  verify(commentsView).onFetchStoriesSuccess to work
        verify(commentsView).onFetchCommentsSuccess(hackerNewsCommentList, 2);
    }

    @Test
    public void HNCommentsPresenterShowLoadMoreViewTest() {

        // Test when there's more than 10 comments available, "Load more" should show.
        String[] commentsID = new String[] { commentID, commentID, commentID, commentID, commentID, commentID, commentID, commentID, commentID, commentID, commentID };
        List<String> commentsToPullList = new ArrayList<>(Arrays.asList(commentsID));
        commentsToPullList.remove(commentsID.length-1);
        ArrayList hackerNewsCommentList = new ArrayList<>(Arrays.asList(comment, comment, comment, comment, comment, comment, comment, comment, comment, comment));

        when(dataSource.getComments(commentsToPullList)).thenReturn(Observable.fromIterable(commentsToPullList).flatMap(id -> dataSource.getComment(id)).toList());
        when(dataSource.getComment(commentID)).thenReturn(Observable.just(comment));

        //commentsPresenter.setView(commentsView);
        commentsPresenter.loadComments(commentsID);
        verify(dataSource).getComments(commentsToPullList);

        dataSource.getComments(commentsToPullList).subscribe();
        verify(commentsView).onFetchCommentsSuccess(hackerNewsCommentList, 10);
        verify(commentsView).showLoadMore();
    }

    @Test
    public void HNCommentsPresenterLoadTopStoriesErrorTest() {

        Exception exception = new Exception();
        // When API getTopStories() is called, return error.
        //when(dataSource.getComment(commentID)).thenReturn(Observable.just(comment));
        when(dataSource.getComments(commentsToPullList)).thenReturn(Single.fromObservable(Observable.error(exception)));//Observable.fromIterable(commentsToPullList).flatMap(id -> dataSource.getComment(id)).toList());
        //bellroywhen(dataSource.getComment(commentID)).thenReturn(Observable.just(comment));
        //when(dataSource.getComment(commentID)).thenReturn(Observable.error(exception));

        // When setView(View view) is called, verify loadNewStories is called.
        commentsPresenter.loadComments(commentsID);
        verify(dataSource).getComments(commentsToPullList);

        //commentsPresenter.loadMoreComments();
        dataSource.getComments(commentsToPullList).subscribe(hackerNewsComments -> {}, error -> {});

        verify(commentsView, atLeastOnce()).onFecthStoriesError();
    }
}
