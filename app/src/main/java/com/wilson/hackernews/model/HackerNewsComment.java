package com.wilson.hackernews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO for HackerNews comment
 */

public class HackerNewsComment implements Parcelable
{
    private String id;

    private long time;

    private String text;

    private String parent;

    private String by;

    private String[] kids;

    private String type;

    protected HackerNewsComment(Parcel in) {
        id = in.readString();
        time = in.readLong();
        text = in.readString();
        parent = in.readString();
        by = in.readString();
        kids = in.createStringArray();
        type = in.readString();
    }

    public static final Creator<HackerNewsComment> CREATOR = new Creator<HackerNewsComment>() {
        @Override
        public HackerNewsComment createFromParcel(Parcel in) {
            return new HackerNewsComment(in);
        }

        @Override
        public HackerNewsComment[] newArray(int size) {
            return new HackerNewsComment[size];
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

    public long getTime ()
    {
        return time;
    }

    public void setTime (long time)
    {
        this.time = time;
    }

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getParent ()
    {
        return parent;
    }

    public void setParent (String parent)
    {
        this.parent = parent;
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

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", time = "+time+", text = "+text+", parent = "+parent+", by = "+by+", kids = "+kids+", type = "+type+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(text);
        dest.writeLong(time);
        dest.writeString(parent);
        dest.writeString(by);
        dest.writeStringArray(kids);
        dest.writeString(type);
    }
}