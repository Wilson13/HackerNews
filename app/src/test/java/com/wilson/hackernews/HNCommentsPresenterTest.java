package com.wilson.hackernews;

import com.google.gson.Gson;
import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.mvp.HNCommentsPresenter;
import com.wilson.hackernews.mvp.HackerNewsAPI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HNCommentsPresenterTest {

    @Mock
    private CommentsViewImpl commentsView;
    @Mock
    private HackerNewsAPI apiService;

    private String[] commentsID;
    private String commentID;

    private HackerNewsComment comment;

    //@Mock
    //HNStoriesPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());

        commentsID = new String[] {"15702188", "15684534"};
        commentID = "15702188";
        comment = new Gson().fromJson("{\"by\" : \"iamleppert\", \"id\" : 15702188, \"kids\" : [ 15702419, 15702707, 15702622, 15705397, 15702670 ],\"parent\" : 15701417, \"text\" : \"Anyone who has worked in real web development knows that source maps are barely functional. When they do work, they are slow and there&#x27;s a non-trivial delay, during which time the minified source or transpiled source or whatever is shown.<p>There&#x27;s a 50&#x2F;50 shot they won&#x27;t even work at all, and of that 50% of the time they do work, they often don&#x27;t have the correct line and column.<p>Again, things like the interactive debugger and &quot;pause on caught exceptions&quot; often casually break with source maps.<p>In short, it&#x27;s a mess.\", \"time\" : 1510732926, \"type\" : \"comment\"}", HackerNewsComment.class);
    }

    @Test
    public void HNCommentsPresenterLoadCommentsSuccessTest() {

        // When API getComment(String commentID) is called, return the comment object defined.
        //when(apiService.getComment(commentID)).thenReturn(Observable.just(comment));

        // When setView(View view) is called, verify loadNewStories is called.
        HNCommentsPresenter spy = Mockito.spy(new HNCommentsPresenter(apiService));
        spy.setView(commentsView);
        spy.loadComments(commentsID);
        verify(spy).loadComments(any());
    }
}
