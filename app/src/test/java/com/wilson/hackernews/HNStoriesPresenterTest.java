package com.wilson.hackernews;

import com.google.gson.Gson;
import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.mvp.GetHackerNewsContract;
import com.wilson.hackernews.mvp.HNStoriesPresenter;
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
public class HNStoriesPresenterTest {

    @Mock
    private GetHackerNewsContract.StoriesView storiesView;
    @Mock
    private HackerNewsModel dataSource;
    @InjectMocks
    private HNStoriesPresenter presenter = new HNStoriesPresenter(dataSource);

    private String storyID;
    private String storyID2;
    private String[] topStoriesID;
    private List<String> storiesToPullList = new ArrayList<>();
    private ArrayList hackerNewsStoryList;

    private HackerNewsStory story;
    private HackerNewsStory story2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

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

        storyID = "15683275";
        storyID2 = "15701417";
        topStoriesID = new String[] {storyID, storyID2};
        storiesToPullList = Arrays.asList(topStoriesID);

        story = new Gson().fromJson("{ \"by\" : \"heshamg\", \"descendants\" : 59, \"id\" : 15683275, \"kids\" : [ 15684947, 15684534, 15684418, 15683718, 15683523, 15683499, 15684849, 15683566, 15683461, 15683463, 15683579, 15684300, 15683851, 15684266, 15683674 ], \"score\" : 141, \"time\" : 1510531519, \"title\" : \"Uber confirms SoftBank has agreed to invest\", \"type\" : \"story\", \"url\" : \"https://techcrunch.com/2017/11/12/uber-confirms-softbank-has-agreed-to-invest-billions-in-uber/\"}", HackerNewsStory.class);
        story2 = new Gson().fromJson("{ \"by\" : \"aeontech\", \"descendants\" : 32, \"id\" : 15701417, \"kids\" : [ 15702188, 15702224, 15701593, 15701728, 15701764, 15701658, 15702192 ], \"score\" : 55, \"time\" : 1510719453, \"title\" : \"WTF is a Source Map\", \"type\" : \"story\", \"url\" : \"https://www.schneems.com/2017/11/14/wtf-is-a-source-map/\"}", HackerNewsStory.class);
        hackerNewsStoryList = new ArrayList<>(Arrays.asList(story, story2));
    }

    @Test
    public void HNStoriesPresenterLoadTopStoriesSuccessViewTest() {

        // When API getTopStories() is called, return the topStoriesID array defined.
        when(dataSource.getTopStories()).thenReturn(Single.fromObservable(Observable.just(topStoriesID)));

        // When API getTopStories() is called, return the hackerNewsStoryList defined.
        when(dataSource.getStories(storiesToPullList)).thenReturn(Observable.fromIterable(storiesToPullList).flatMap(id -> dataSource.getStory(id)).toList());

        when(dataSource.getStory(storyID)).thenReturn(Observable.just(story));
        when(dataSource.getStory(storyID2)).thenReturn(Observable.just(story2));

        // When setView(View view) is called, verify loadNewStories is called.
        presenter.setView(storiesView);
        presenter.loadNewStories();
        verify(dataSource).getTopStories();
        verify(storiesView, atLeastOnce()).onFetchStoriesStart();
        verify(storiesView, atLeastOnce()).onFetchStoriesStart();
        verify(dataSource).getStories(storiesToPullList);
        dataSource.getStories(storiesToPullList).subscribe(); // This line is required for  verify(storiesView).onFetchStoriesSuccess to work
        verify(storiesView).onFetchStoriesSuccess(hackerNewsStoryList, 2);
        verify(storiesView).hideLoadMore();
    }

    @Test
    public void HNStoriesPresenterShowLoadMoreViewTest() {
        String[] topStoriesID = new String[] {storyID, storyID, storyID, storyID, storyID, storyID, storyID, storyID, storyID, storyID, storyID };
        List<String> storiesToPullList = new ArrayList<>(Arrays.asList(topStoriesID));
        storiesToPullList.remove(topStoriesID.length-1);
        ArrayList hackerNewsStoryList = new ArrayList<>(Arrays.asList(story, story, story, story, story, story, story, story, story, story));

        // When API getTopStories() is called, return the topStoriesID array defined.
        when(dataSource.getTopStories()).thenReturn(Single.fromObservable(Observable.just(topStoriesID)));

        // When API getStories(storiesToPullList) is called, return the hackerNewsStoryList ArrayList defined.
        when(dataSource.getStories(storiesToPullList)).thenReturn(Observable.fromIterable(storiesToPullList).flatMap(id -> dataSource.getStory(id)).toList());

        // When API getStory(storyID) is called, return the story object defined.
        when(dataSource.getStory(storyID)).thenReturn(Observable.just(story));

        presenter.setView(storiesView);
        presenter.loadNewStories();
        verify(dataSource).getTopStories();
        verify(storiesView, atLeastOnce()).onFetchStoriesStart();
        verify(storiesView, atLeastOnce()).onFetchStoriesStart();
        verify(dataSource).getStories(storiesToPullList);

        dataSource.getStories(storiesToPullList).subscribe(); // This line is required for  verify(storiesView).onFetchStoriesSuccess to work
        verify(storiesView).onFetchStoriesSuccess(hackerNewsStoryList, 10);
        verify(storiesView).showLoadMore();
    }

    @Test
    public void HNStoriesPresenterLoadTopStoriesEmptyViewTest() {

        ArrayList hackerNewsStoryList = new ArrayList();

        // When API getTopStories() is called, return the topStoriesID array defined.
        when(dataSource.getTopStories()).thenReturn(Single.fromObservable(Observable.just(topStoriesID)));

        // When API getTopStories(storiesToPullList) is called, return the hackerNewsStoryList defined.
        when(dataSource.getStories(storiesToPullList)).thenReturn(Single.fromObservable(Observable.fromIterable(hackerNewsStoryList)));

        presenter.setView(storiesView);
        presenter.loadNewStories();
        verify(dataSource).getTopStories();
        verify(storiesView, atLeastOnce()).onFetchStoriesStart();
        verify(storiesView, atLeastOnce()).onFetchStoriesStart();
        verify(dataSource).getStories(storiesToPullList);

        dataSource.getStories(storiesToPullList).subscribe(hackerNewsStories -> {}, error -> {});
        verify(storiesView).onFetchStoriesError();
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
        verify(storiesView, atLeastOnce()).onFetchStoriesError();
    }
}
