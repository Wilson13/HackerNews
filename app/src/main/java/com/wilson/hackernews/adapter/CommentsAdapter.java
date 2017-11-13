package com.wilson.hackernews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wilson.hackernews.R;
import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.other.MyApp;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.HackerNewsCommentHolder> {

    private static final String TAG = "CommentsAdapter";
    private List<HackerNewsComment> hackerNewsCommentsList;
    private Context context;

    public CommentsAdapter(List<HackerNewsComment> hackerNewsCommentsList) {
        this.hackerNewsCommentsList = hackerNewsCommentsList;
        context = MyApp.get().getApplicationContext();
    }

    @Override
    public HackerNewsCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_hacker_comments, parent, false);
        return new HackerNewsCommentHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final HackerNewsCommentHolder holder, int position) {
        // Get story from HackerNews story list
        HackerNewsComment currentItem = hackerNewsCommentsList.get(position);
        Log.d(TAG, "onBindViewHolder currentItem parent: " + currentItem.getParent());

        String content = currentItem.getText();
        String byName = currentItem.getBy();
        String[] kids = currentItem.getKids();
        int time = currentItem.getTime();

        // Display the information
        holder.properties.setText(context.getString(R.string.comment_properties, byName, "1 hour"));
        holder.content.setText(content);

//        holder.comments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "clicked!");
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return hackerNewsCommentsList.size();
    }

    class HackerNewsCommentHolder extends RecyclerView.ViewHolder {

        TextView properties;
        TextView content;
        TextView viewReplies;

        HackerNewsCommentHolder(View itemView) {
            super(itemView);
            properties = (TextView) itemView.findViewById(R.id.tv_properties);
            content = (TextView) itemView.findViewById(R.id.tv_comment_content);
            viewReplies = (TextView) itemView.findViewById(R.id.tv_view_replies);
        }
    }
}
