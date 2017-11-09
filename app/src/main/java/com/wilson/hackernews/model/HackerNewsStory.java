package com.wilson.hackernews.model;

/**
 * POJO for HackerNewsStory (item of HackerNews API)
 */

public class HackerNewsStory
{
    private String id;

    private String title;

    private int time;

    private String score;

    private String descendants;

    private String by;

    private String[] kids;

    private String type;

    private String url;

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

    public int getTime ()
    {
        return time;
    }

    public void setTime (int time)
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

    public String getDescendants ()
    {
        return descendants;
    }

    public void setDescendants (String descendants)
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
}
