package worker8.com.github.jimgur.imgur.gallery_api;

import java.io.Serializable;

public class Comment_preview implements Serializable
{
    private String platform;

    private String vote;

    private String album_cover;

    private String[] children;

    private String on_album;

    private String deleted;

    private String image_id;

    private String id;

    private String author;

    private String author_id;

    private String downs;

    private String points;

    private String comment;

    private String ups;

    private String datetime;

    private String parent_id;

    public String getPlatform ()
    {
        return platform;
    }

    public void setPlatform (String platform)
    {
        this.platform = platform;
    }

    public String getVote ()
    {
      return vote;
    }

    public void setVote (String vote)
    {
        this.vote = vote;
    }

    public String getAlbum_cover ()
    {
        return album_cover;
    }

    public void setAlbum_cover (String album_cover)
    {
        this.album_cover = album_cover;
    }

    public String[] getChildren ()
    {
        return children;
    }

    public void setChildren (String[] children)
    {
        this.children = children;
    }

    public String getOn_album ()
    {
        return on_album;
    }

    public void setOn_album (String on_album)
    {
        this.on_album = on_album;
    }

    public String getDeleted ()
    {
        return deleted;
    }

    public void setDeleted (String deleted)
    {
        this.deleted = deleted;
    }

    public String getImage_id ()
    {
        return image_id;
    }

    public void setImage_id (String image_id)
    {
        this.image_id = image_id;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getAuthor ()
    {
        return author;
    }

    public void setAuthor (String author)
    {
        this.author = author;
    }

    public String getAuthor_id ()
    {
        return author_id;
    }

    public void setAuthor_id (String author_id)
    {
        this.author_id = author_id;
    }

    public String getDowns ()
    {
        return downs;
    }

    public void setDowns (String downs)
    {
        this.downs = downs;
    }

    public String getPoints ()
    {
        return points;
    }

    public void setPoints (String points)
    {
        this.points = points;
    }

    public String getComment ()
    {
        return comment;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
    }

    public String getUps ()
    {
        return ups;
    }

    public void setUps (String ups)
    {
        this.ups = ups;
    }

    public String getDatetime ()
    {
        return datetime;
    }

    public void setDatetime (String datetime)
    {
        this.datetime = datetime;
    }

    public String getParent_id ()
    {
        return parent_id;
    }

    public void setParent_id (String parent_id)
    {
        this.parent_id = parent_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [platform = "+platform+", vote = "+vote+", album_cover = "+album_cover+", children = "+children+", on_album = "+on_album+", deleted = "+deleted+", image_id = "+image_id+", id = "+id+", author = "+author+", author_id = "+author_id+", downs = "+downs+", points = "+points+", comment = "+comment+", ups = "+ups+", datetime = "+datetime+", parent_id = "+parent_id+"]";
    }
}
