package com.wilson.hackernews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO for HackerNews story
 */

public class HackerNewsStory implements Parcelable {
    private String id;

    private String title;

    private long time;

    private String score;

    private int descendants;

    private String by;

    private String[] kids;

    private String type;

    private String url;

    public HackerNewsStory(Parcel in) {
        id = in.readString();
        title = in.readString();
        time = in.readInt();
        score = in.readString();
        descendants = in.readInt();
        by = in.readString();
        kids = in.createStringArray();
        type = in.readString();
        url = in.readString();
    }

    public static final Creator<HackerNewsStory> CREATOR = new Creator<HackerNewsStory>() {
        @Override
        public HackerNewsStory createFromParcel(Parcel in) {
            return new HackerNewsStory(in);
        }

        @Override
        public HackerNewsStory[] newArray(int size) {
            return new HackerNewsStory[size];
        }
    };

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public long getTime ()
    {
        return time;
    }

    public void setTime (long time)
    {
        this.time = time;
    }

    public String getScore ()
    {
        return score;
    }

    public void setScore (String score)
    {
        this.score = score;
    }

    public int getDescendants ()
    {
        return descendants;
    }

    public void setDescendants (int descendants)
    {
        this.descendants = descendants;
    }

    public String getBy ()
    {
        return by;
    }

    public void setBy (String by)
    {
        this.by = by;
    }

    public String[] getKids ()
    {
        return kids;
    }

    public void setKids (String[] kids)
    {
        this.kids = kids;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", title = "+title+", time = "+time+", score = "+score+", descendants = "+descendants+", by = "+by+", kids = "+kids+", type = "+type+", url = "+url+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeLong(time);
        dest.writeString(score);
        dest.writeInt(descendants);
        dest.writeString(by);
        dest.writeStringArray(kids);
        dest.writeString(type);
        dest.writeString(url);
    }
}
