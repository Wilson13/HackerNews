package com.wilson.hackernews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wilson.hackernews.R;
import com.wilson.hackernews.model.HackerNewsStory;
import com.wilson.hackernews.other.MyApp;

import java.util.List;

public class TopStoriesAdapter extends RecyclerView.Adapter<TopStoriesAdapter.HackerNewsStoryHolder> {

    private static final String TAG = "TopStoriesAdapter";
    private List<HackerNewsStory> hackerNewsStoryList;
    private Context context;
    private CommentsClickedListener delegate;

    // Interface to pass comments click event to parent TopStoriesFragment
    public interface CommentsClickedListener {
        void commentsClicked(String[] commentsID);
    }

    public TopStoriesAdapter() {
        context = MyApp.get().getApplicationContext();
    }

    @Override
    public HackerNewsStoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_hacker_news_story, parent, false);
        Log.d(TAG, "onCreateViewHolder");
        return new HackerNewsStoryHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(HackerNewsStoryHolder holder, int position) {
        // Get story from HackerNews story list
        HackerNewsStory currentItem = hackerNewsStoryList.get(position);
        Log.d(TAG, "onBindViewHolder currentItem: " + currentItem.getTitle());

        String title = currentItem.getTitle();
        String score = currentItem.getScore();
        String byName = currentItem.getBy();
        String comments = currentItem.getDescendants();
        final String[] kids = currentItem.getKids();

        String timeCommented ="";
        long time = currentItem.getTime();
        long currentTime = System.currentTimeMillis() / 1000;

        Log.d(TAG, "time: " + time);
        Log.d(TAG, "currentTime: " + currentTime);
        long commentedTime = currentTime - time;

        if (commentedTime > (60 * 60)) {
            long commentTimeHour = commentedTime / (60 * 60);

            if (commentTimeHour > 1)
                timeCommented = commentTimeHour + " hours";
            else
                timeCommented = commentTimeHour + " hour";

        } else if (commentedTime > (60)) {
            timeCommented = commentedTime / (60) + " mins";
        } else if (commentedTime > (1000)) {
            timeCommented = commentedTime + " secs";
        } else {
            timeCommented = 0 + " sec";
        }

        // Display the information
        holder.title.setText(title);
        holder.properties.setText(context.getString(R.string.story_properties, score, byName, timeCommented));
        holder.comments.setText(context.getString(R.string.story_comments, comments));
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null)
                    delegate.commentsClicked(kids);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hackerNewsStoryList.size();
    }

    public void setData(List<HackerNewsStory> hackerNewsStoryList) {
        this.hackerNewsStoryList = hackerNewsStoryList;
    }

    public void setDelegate(CommentsClickedListener delegate) {
        this.delegate = delegate;
    }

    class HackerNewsStoryHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView properties;
        TextView comments;

        HackerNewsStoryHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            properties = (TextView) itemView.findViewById(R.id.tv_properties);
            comments = (TextView) itemView.findViewById(R.id.tv_comments);
        }
    }
}
