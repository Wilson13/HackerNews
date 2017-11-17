package com.wilson.hackernews;

import com.google.gson.Gson;
import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.mvp.GetHackerNewsContract;
import com.wilson.hackernews.mvp.HNStoriesPresenter;
import com.wilson.hackernews.mvp.HackerNewsAPI;
import com.wilson.hackernews.mvp.HackerNewsAPIDataSource;

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
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HNStoriesPresenterTest {

    @Mock
    private GetHackerNewsContract.StoriesView storiesView;
    @Mock
    private HackerNewsAPIDataSource dataSource;
    @Mock
    private HackerNewsAPI apiService;
    @InjectMocks
    HNStoriesPresenter presenter;

    private String[] topStoriesID;
    private List<String> storiesToPullList = new ArrayList<>();
    private String[] commentsID;
    private String storyID;
    private String storyID2;
    private String commentID;

    HackerNewsStory story;
    HackerNewsStory story2;
    HackerNewsComment comment;
    ArrayList hackerNewsStoryList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());

        presenter = new HNStoriesPresenter(dataSource);
        topStoriesID = new String[] {"15683275", "15701417"};
        storyID = "15683275";
        storyID2 = "15701417";
        storiesToPullList = Arrays.asList(topStoriesID);
        story = new Gson().fromJson("{ \"by\" : \"heshamg\", \"descendants\" : 59, \"id\" : 15683275, \"kids\" : [ 15684947, 15684534, 15684418, 15683718, 15683523, 15683499, 15684849, 15683566, 15683461, 15683463, 15683579, 15684300, 15683851, 15684266, 15683674 ], \"score\" : 141, \"time\" : 1510531519, \"title\" : \"Uber confirms SoftBank has agreed to invest\", \"type\" : \"story\", \"url\" : \"https://techcrunch.com/2017/11/12/uber-confirms-softbank-has-agreed-to-invest-billions-in-uber/\"}", HackerNewsStory.class);
        story2 = new Gson().fromJson("{ \"by\" : \"aeontech\", \"descendants\" : 32, \"id\" : 15701417, \"kids\" : [ 15702188, 15702224, 15701593, 15701728, 15701764, 15701658, 15702192 ], \"score\" : 55, \"time\" : 1510719453, \"title\" : \"WTF is a Source Map\", \"type\" : \"story\", \"url\" : \"https://www.schneems.com/2017/11/14/wtf-is-a-source-map/\"}", HackerNewsStory.class);
        hackerNewsStoryList = new ArrayList<>(Arrays.asList(story, story2));

        commentsID = new String[] {"15702188", "15684534"};
        commentID = "15702188";
        comment = new Gson().fromJson("{\"by\" : \"iamleppert\", \"id\" : 15702188, \"kids\" : [ 15702419, 15702707, 15702622, 15705397, 15702670 ],\"parent\" : 15701417, \"text\" : \"Anyone who has worked in real web development knows that source maps are barely functional. When they do work, they are slow and there&#x27;s a non-trivial delay, during which time the minified source or transpiled source or whatever is shown.<p>There&#x27;s a 50&#x2F;50 shot they won&#x27;t even work at all, and of that 50% of the time they do work, they often don&#x27;t have the correct line and column.<p>Again, things like the interactive debugger and &quot;pause on caught exceptions&quot; often casually break with source maps.<p>In short, it&#x27;s a mess.\", \"time\" : 1510732926, \"type\" : \"comment\"}", HackerNewsComment.class);
    }

    @Test
    public void HNStoriesPresenterLoadTopStoriesSuccessViewTest() {

        // When API getTopStories() is called, return the topStoriesID array defined.
        when(dataSource.getTopStories()).thenReturn(Single.fromObservable(Observable.just(topStoriesID)));

        // When API getTopStories() is called, return the hackerNewsStoryList defined.
        when(dataSource.getStories(storiesToPullList)).thenReturn(Observable.fromIterable(storiesToPullList).flatMap(id -> dataSource.getStory(id)).toList());

                //Single.fromObservable(Observable.fromIterable(hackerNewsStoryList)));

        when(dataSource.getStory(storyID)).thenReturn(Observable.just(story));
        when(dataSource.getStory(storyID2)).thenReturn(Observable.just(story2));

        // When setView(View view) is called, verify loadNewStories is called.
        //HNStoriesPresenter presenter = new HNStoriesPresenter(dataSource); // Mockito.spy(new HNStoriesPresenter(dataSource));
        //HNStoriesPresenter presenter = Mockito.spy(new HNStoriesPresenter(dataSource));
        presenter.setView(storiesView);
        //verify(presenter).loadNewStories();
        verify(dataSource).getTopStories();
        verify(storiesView, atLeastOnce()).onFetchStoriesStart();
        //verify(presenter).loadMoreStories();
        verify(storiesView, atLeastOnce()).onFetchStoriesStart();
        verify(dataSource).getStories(storiesToPullList);
        dataSource.getStories(storiesToPullList).subscribe(); // This line is required for  verify(storiesView).onFetchStoriesSuccess to work
        verify(storiesView).onFetchStoriesSuccess(hackerNewsStoryList);
        verify(storiesView).hideLoadMore();
    }

//    @Test
//    public void HNStoriesPresenterLoadMoreStoriesSuccessViewTest() {
//
//        // When API getTopStories() is called, return the topStoriesID array defined.
////        when(dataSource.getTopStories()).thenReturn(Single.fromObservable(Observable.just(topStoriesID)));
////
////        // When API getTopStories() is called, return the hackerNewsStoryList defined.
////        when(dataSource.getStories(storiesToPullList)).thenReturn(Single.fromObservable(Observable.fromIterable(hackerNewsStoryList)));
////
////        //Observable.fromIterable(storiesToPullList).flatMap(id -> dataSource.getStory(id)).toList());
////
////        //when(dataSource.getStory(storyID)).thenReturn(Observable.just(story));
////        //when(dataSource.getStory(storyID2)).thenReturn(Observable.just(story2));
////
////        // Initial set up
////        HNStoriesPresenter presenter = new HNStoriesPresenter(dataSource);
////        presenter.setView(storiesView);
////        verify(storiesView, times(2)).onFetchStoriesStart();
////
////        presenter.loadMoreStories();
////        verify(storiesView).onFetchStoriesStart();
////        verify(dataSource).getStories(storiesToPullList);
////        verify(storiesView).onFetchStoriesSuccess(hackerNewsStoryList);
//    }

    @Test
    public void HNStoriesPresenterLoadTopStoriesEmptyViewTest() {

        ArrayList hackerNewsStoryList = new ArrayList();

        // When API getTopStories() is called, return the topStoriesID array defined.
        when(dataSource.getTopStories()).thenReturn(Single.fromObservable(Observable.just(topStoriesID)));

        // When API getTopStories() is called, return the hackerNewsStoryList defined.
        when(dataSource.getStories(storiesToPullList)).thenReturn(Single.fromObservable(Observable.fromIterable(hackerNewsStoryList)));

        presenter.setView(storiesView);
        verify(dataSource).getTopStories();
        verify(storiesView, times(2)).onFetchStoriesStart();
        verify(dataSource).getStories(storiesToPullList);
        verify(storiesView).onFecthStoriesError();
    }

    @Test
    public void HNStoriesPresenterLoadTopStoriesErrorTest() {

        Exception exception = new Exception();
        // When API getTopStories() is called, return error.
        when(dataSource.getTopStories()).thenReturn(Single.fromObservable(Observable.error(exception)));

        // When setView(View view) is called, verify loadNewStories is called.
        HNStoriesPresenter presenter = new HNStoriesPresenter(dataSource);
        presenter.setView(storiesView);
        presenter.loadNewStories();
        verify(storiesView, atLeastOnce()).onFecthStoriesError();
    }
}
