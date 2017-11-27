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
import com.wilson.hackernews.other.Utils;

import java.util.List;

import static com.wilson.hackernews.other.Utils.getCorrectURL;

public class TopStoriesAdapter extends RecyclerView.Adapter<TopStoriesAdapter.HackerNewsStoryHolder> {

    private static final String TAG = "TopStoriesAdapter";
    private List<HackerNewsStory> hackerNewsStoryList;
    private Context context;
    private CommentsClickedListener delegate;

    // Interface to pass comments click event to parent TopStoriesFragment
    public interface CommentsClickedListener {
        void storyClicked(String url);

        void commentsClicked(String[] commentsID);
    }

    public TopStoriesAdapter(CommentsClickedListener delegate) {
        context = MyApp.get().getApplicationContext();
        this.delegate = delegate;
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

        final String title = currentItem.getTitle();
        String score = currentItem.getScore();
        String byName = currentItem.getBy();
        int numComments = currentItem.getDescendants();

        // Unix time is in seconds
        String timePosted = Utils.getElapsedTime(currentItem.getTime(), System.currentTimeMillis() / 1000);

        // Display the information
        holder.title.setText(title);
        holder.properties.setText(context.getString(R.string.story_properties, score, byName, timePosted));

        if (numComments > 0)
            holder.comments.setText(numComments > 1 ? numComments + " comments" : numComments + " comment");
        else
            holder.comments.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return hackerNewsStoryList.size();
    }

    public void setData(List<HackerNewsStory> hackerNewsStoryList) {
        this.hackerNewsStoryList = hackerNewsStoryList;
    }

    class HackerNewsStoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView properties;
        TextView comments;

        HackerNewsStoryHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            properties = (TextView) itemView.findViewById(R.id.tv_properties);
            comments = (TextView) itemView.findViewById(R.id.tv_comments);
            comments.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // onBindViewHolder() is called for each and every item and setting the click listener
            // is an unnecessary option to repeat when we call it once in the ViewHolder constructor.
            // https://stackoverflow.com/questions/33845846/why-is-adding-an-onclicklistener-inside-onbindviewholder-of-a-recyclerview-adapt

            HackerNewsStory currentItem = hackerNewsStoryList.get(getAdapterPosition());
            int numComments = currentItem.getDescendants();
            String[] kids = currentItem.getKids();
            String url = currentItem.getUrl();

            // Handle click on comments
            if (v.getId() == R.id.tv_comments) {
                if (numComments > 0) {
                    Log.d(TAG, "title: " + title + " kids size: " + kids.length);
                    delegate.commentsClicked(kids);
                }
            }
            // Handle click on RecyclerView item
            else {
                // In case URL doesn't start with http or https
                url = getCorrectURL(url);
                delegate.storyClicked(url);
            }
        }
    }
}
