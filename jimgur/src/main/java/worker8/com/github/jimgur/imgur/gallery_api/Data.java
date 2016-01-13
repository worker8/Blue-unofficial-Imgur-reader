package worker8.com.github.jimgur.imgur.gallery_api;

import java.io.Serializable;

import worker8.com.github.jimgur.imgur.album_api.Images;


public class Data implements Serializable {
    private String link;

    private String score;

    private String id;

    private String cover;

    private String title;

    private String description;

    private String layout;

    private String cover_height;

    private String points;

    private String topic_id;

    private String account_url;

    private String cover_width;

    private String datetime;

    private String topic;

    private String comment_count;

    private String account_id;

    private String vote;

    private String privacy;

    private String favorite;

    private String section;

    private Comment_preview[] comment_preview;

    private String nsfw;

    private String views;

    private Images[] images;

    private String downs;

    private String is_album;

    private String ups;

    private String images_count;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getCover_height() {
        return cover_height;
    }

    public void setCover_height(String cover_height) {
        this.cover_height = cover_height;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getAccount_url() {
        return account_url;
    }

    public void setAccount_url(String account_url) {
        this.account_url = account_url;
    }

    public String getCover_width() {
        return cover_width;
    }

    public void setCover_width(String cover_width) {
        this.cover_width = cover_width;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Comment_preview[] getComment_preview() {
        return comment_preview;
    }

    public void setComment_preview(Comment_preview[] comment_preview) {
        this.comment_preview = comment_preview;
    }

    public String getNsfw() {
        return nsfw;
    }

    public void setNsfw(String nsfw) {
        this.nsfw = nsfw;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public Images[] getImages() {
        return images;
    }

    public void setImages(Images[] images) {
        this.images = images;
    }

    public String getDowns() {
        return downs;
    }

    public void setDowns(String downs) {
        this.downs = downs;
    }

    public String getIs_album() {
        return is_album;
    }

    public void setIs_album(String is_album) {
        this.is_album = is_album;
    }

    public String getUps() {
        return ups;
    }

    public void setUps(String ups) {
        this.ups = ups;
    }

    public String getImages_count() {
        return images_count;
    }

    public void setImages_count(String images_count) {
        this.images_count = images_count;
    }

    @Override
    public String toString() {
        return "ClassPojo [link = " + link + ", score = " + score + ", id = " + id + ", cover = " + cover + ", title = " + title + ", description = " + description + ", layout = " + layout + ", cover_height = " + cover_height + ", points = " + points + ", topic_id = " + topic_id + ", account_url = " + account_url + ", cover_width = " + cover_width + ", datetime = " + datetime + ", topic = " + topic + ", comment_count = " + comment_count + ", account_id = " + account_id + ", vote = " + vote + ", privacy = " + privacy + ", favorite = " + favorite + ", section = " + section + ", comment_preview = " + comment_preview + ", nsfw = " + nsfw + ", views = " + views + ", images = " + images + ", downs = " + downs + ", is_album = " + is_album + ", ups = " + ups + ", images_count = " + images_count + "]";
    }
}
