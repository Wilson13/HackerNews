package com.wilson.hackernews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wilson.hackernews.R;
import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.other.MyApp;
import com.wilson.hackernews.other.Utils;

import java.util.List;

import static com.wilson.hackernews.other.Utils.getStyledText;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.HackerNewsCommentHolder> {

    private static final String TAG = "CommentsAdapter";
    private List<HackerNewsComment> hackerNewsCommentsList;
    private Context context;

    public RepliesAdapter() {
        context = MyApp.get().getApplicationContext();
    }

    @Override
    public HackerNewsCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_hacker_replies, parent, false);
        return new HackerNewsCommentHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final HackerNewsCommentHolder holder, int position) {
        // Get story from HackerNews story list
        HackerNewsComment currentItem = hackerNewsCommentsList.get(position);

        String content = currentItem.getText();
        String byName = currentItem.getBy();
        long time = currentItem.getTime();

        // Unix time is in seconds
        String timeReplied = Utils.getElapsedTime(time, System.currentTimeMillis() / 1000);
        // Display the information
        holder.properties.setText(context.getString(R.string.comment_properties, byName, timeReplied));

        // Display comments with HTML entities
        if (!TextUtils.isEmpty(content))
            holder.content.setText(getStyledText(content));
    }

    @Override
    public int getItemCount() {
        return hackerNewsCommentsList.size();
    }

    public void setComments(List<HackerNewsComment> hackerNewsCommentsList) {
        this.hackerNewsCommentsList = hackerNewsCommentsList;
    }

    class HackerNewsCommentHolder extends RecyclerView.ViewHolder {

        TextView properties;
        TextView content;

        HackerNewsCommentHolder(View itemView) {
            super(itemView);
            properties = (TextView) itemView.findViewById(R.id.tv_comment_properties);
            content = (TextView) itemView.findViewById(R.id.tv_comment_content);
        }
    }
}
