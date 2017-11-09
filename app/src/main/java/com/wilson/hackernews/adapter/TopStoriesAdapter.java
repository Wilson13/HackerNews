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

    public TopStoriesAdapter(List<HackerNewsStory> hackerNewsStoryList) {
        this.hackerNewsStoryList = hackerNewsStoryList;
        context = MyApp.get().getApplicationContext();
    }

    @Override
    public HackerNewsStoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_hacker_news_story, parent, false);
        return new HackerNewsStoryHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final HackerNewsStoryHolder holder, int position) {
        // Get story from HackerNews story list
        HackerNewsStory currentItem = hackerNewsStoryList.get(position);
        Log.d(TAG, "onBindViewHolder currentItem: " + currentItem.getTitle());

        String title = currentItem.getTitle();
        String score = currentItem.getScore();
        String byName = currentItem.getBy();
        String comments = currentItem.getDescendants();

        // Display the information
        holder.title.setText(title);
        holder.properties.setText(context.getString(R.string.story_properties, score, byName));
        holder.comments.setText(comments);
    }

    @Override
    public int getItemCount() {
        return hackerNewsStoryList.size();
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
