package com.wilson.hackernews.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wilson.hackernews.R;
import com.wilson.hackernews.model.HackerNewsComment;
import com.wilson.hackernews.other.MyApp;
import com.wilson.hackernews.other.Utils;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.HackerNewsCommentHolder> {

    private static final String TAG = "CommentsAdapter";
    private List<HackerNewsComment> hackerNewsCommentsList;
    private Context context;
    private RepliesClickedListener delegate;

    // Interface to pass repplies click event to parent CommentsFragment
    public interface RepliesClickedListener {
        void repliesClicked(String[] repliesID);
    }

    public CommentsAdapter(RepliesClickedListener delegate) {
        context = MyApp.get().getApplicationContext();
        this.delegate = delegate;
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
        final String[] kids = currentItem.getKids();
        long time = currentItem.getTime();

        // Unix time is in seconds
        String timeCommented = Utils.getElapsedTime(time, System.currentTimeMillis() / 1000);

        // Display the information
        holder.properties.setText(context.getString(R.string.comment_properties, byName, timeCommented));

        // Display comments with HTML entities
        if (content != null) {
            if (Build.VERSION.SDK_INT >= 24)
                holder.content.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
            else
                holder.content.setText(Html.fromHtml(content));
        }

        // Display "View replies" if there are replies to this comment
        // (showing only 1 level as stated in the requirement).
        if (kids == null || kids.length < 0)
            holder.viewReplies.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return hackerNewsCommentsList.size();
    }

    public void setComments(List<HackerNewsComment> hackerNewsCommentsList) {
        this.hackerNewsCommentsList = hackerNewsCommentsList;
    }

    public class HackerNewsCommentHolder extends RecyclerView.ViewHolder {

        TextView properties;
        TextView content;
        TextView viewReplies;
        HackerNewsComment currentItem;

        HackerNewsCommentHolder(View itemView) {
            super(itemView);
            properties = (TextView) itemView.findViewById(R.id.tv_comment_properties);
            content = (TextView) itemView.findViewById(R.id.tv_comment_content);
            viewReplies = (TextView) itemView.findViewById(R.id.tv_view_replies);

            if (getItemCount() > getAdapterPosition() && getAdapterPosition() > 0) {
                currentItem = hackerNewsCommentsList.get(getAdapterPosition());
                String[] kids = currentItem.getKids();
                viewReplies.setOnClickListener(v -> delegate.repliesClicked(kids));
            }
        }
    }
}
