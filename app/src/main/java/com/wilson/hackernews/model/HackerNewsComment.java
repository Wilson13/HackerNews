package com.wilson.hackernews.model;

/**
 * POJO for HackerNews comment
 */

public class HackerNewsComment
{
    private String id;

    private int time;

    private String text;

    private String parent;

    private String by;

    private String[] kids;

    private String type;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public int getTime ()
    {
        return time;
    }

    public void setTime (int time)
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
}