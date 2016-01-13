package worker8.com.github.jimgur.imgur.image_api;

/**
 * Created by tanjunrong on 8/27/15.
 */

public class Data {
    private String animated;

    private String account_id;

    private String vote;

    private String link;
    private String mp4;
    private String gifv;
    private String webm;

    public String getMp4() {
        return mp4;
    }

    public void setMp4(String mp4) {
        this.mp4 = mp4;
    }

    public String getGifv() {
        return gifv;
    }

    public void setGifv(String gifv) {
        this.gifv = gifv;
    }

    public String getWebm() {
        return webm;
    }

    public void setWebm(String webm) {
        this.webm = webm;
    }

    private String width;

    private String favorite;

    private String type;

    private String section;

    private String size;

    private String bandwidth;

    private String comment_preview;

    private String id;

    private String title;

    private String height;

    private String nsfw;

    private String description;

    private String views;

    private String account_url;

    private String datetime;

    public String getAnimated() {
        return animated;
    }

    public void setAnimated(String animated) {
        this.animated = animated;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getComment_preview() {
        return comment_preview;
    }

    public void setComment_preview(String comment_preview) {
        this.comment_preview = comment_preview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getNsfw() {
        return nsfw;
    }

    public void setNsfw(String nsfw) {
        this.nsfw = nsfw;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getAccount_url() {
        return account_url;
    }

    public void setAccount_url(String account_url) {
        this.account_url = account_url;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "ClassPojo [animated = " + animated + ", account_id = " + account_id + ", vote = " + vote + ", link = " + link + ", mp4 = " + mp4 + ", webm = " + webm + ", gifv = " + gifv + ", width = " + width + ", favorite = " + favorite + ", type = " + type + ", section = " + section + ", size = " + size + ", bandwidth = " + bandwidth + ", comment_preview = " + comment_preview + ", id = " + id + ", title = " + title + ", height = " + height + ", nsfw = " + nsfw + ", description = " + description + ", views = " + views + ", account_url = " + account_url + ", datetime = " + datetime + "]";
    }
}

