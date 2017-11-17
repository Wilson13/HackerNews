package com.wilson.hackernews;

import com.google.gson.Gson;
import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.mvp.HackerNewsAPI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class APIServiceTest {

    @Mock
    private HackerNewsAPI apiService;

    private String[] topStoriesID;
    private String storyID;
    private String commentID;

    private HackerNewsStory story;
    private HackerNewsStory story2;
    private HackerNewsComment comment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());

        topStoriesID = new String[]{"15683275", "15701417"};
        storyID = "15683275";
        commentID = "15702188";

        story = new Gson().fromJson("{ \"by\" : \"heshamg\", \"descendants\" : 59, \"id\" : 15683275, \"kids\" : [ 15684947, 15684534, 15684418, 15683718, 15683523, 15683499, 15684849, 15683566, 15683461, 15683463, 15683579, 15684300, 15683851, 15684266, 15683674 ], \"score\" : 141, \"time\" : 1510531519, \"title\" : \"Uber confirms SoftBank has agreed to invest\", \"type\" : \"story\", \"url\" : \"https://techcrunch.com/2017/11/12/uber-confirms-softbank-has-agreed-to-invest-billions-in-uber/\"}", HackerNewsStory.class);
        story2 = new Gson().fromJson("{ \"by\" : \"aeontech\", \"descendants\" : 32, \"id\" : 15701417, \"kids\" : [ 15702188, 15702224, 15701593, 15701728, 15701764, 15701658, 15702192 ], \"score\" : 55, \"time\" : 1510719453, \"title\" : \"WTF is a Source Map\", \"type\" : \"story\", \"url\" : \"https://www.schneems.com/2017/11/14/wtf-is-a-source-map/\"}", HackerNewsStory.class);
        comment = new Gson().fromJson("{\"by\" : \"iamleppert\", \"id\" : 15702188, \"kids\" : [ 15702419, 15702707, 15702622, 15705397, 15702670 ],\"parent\" : 15701417, \"text\" : \"Anyone who has worked in real web development knows that source maps are barely functional. When they do work, they are slow and there&#x27;s a non-trivial delay, during which time the minified source or transpiled source or whatever is shown.<p>There&#x27;s a 50&#x2F;50 shot they won&#x27;t even work at all, and of that 50% of the time they do work, they often don&#x27;t have the correct line and column.<p>Again, things like the interactive debugger and &quot;pause on caught exceptions&quot; often casually break with source maps.<p>In short, it&#x27;s a mess.\", \"time\" : 1510732926, \"type\" : \"comment\"}", HackerNewsComment.class);

        // When API getTopStories() is called, return the topStoriesID array defined.
        when(apiService.getTopStories()).thenReturn(Single.fromObservable(Observable.just(topStoriesID)));

        // When API getStory(String storyID) is called, return the story object defined.
        when(apiService.getStory(storyID)).thenReturn(Observable.just(story));

        // When API getStory(String storyID) is called, return the story object defined.
        when(apiService.getComment(commentID)).thenReturn(Observable.just(comment));
    }

    @Test
    public void APIServiceTest() {

        // Make sure this API service returns the topStoriesID array as defined.
        TestObserver<String []> testObserver;
        testObserver = apiService.getTopStories().test();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValue(topStoriesID);

        // Make sure this API service returns the story as defined.
        TestObserver<HackerNewsStory> testObserver2;
        testObserver2 = apiService.getStory(storyID).test();
        testObserver2.assertNoErrors();
        testObserver2.assertComplete();
        testObserver2.assertValue(story);

        // Make sure this API service returns the comment as defined.
        TestObserver<HackerNewsComment> testObserver3;
        testObserver3 = apiService.getComment(commentID).test();
        testObserver3.assertNoErrors();
        testObserver3.assertComplete();
        testObserver3.assertValue(comment);
    }
}
