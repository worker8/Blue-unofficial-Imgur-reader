package worker8.com.github.jimgur.imgur.album_api;

import java.io.Serializable;

/**
 * generated from http://pojo.sodhanalibrary.com/
 */
public class Data implements Serializable {
    private String account_id;

    private String link;

    private String privacy;

    private String favorite;

    private String section;

    private String id;

    private String title;

    private String cover;

    private String nsfw;

    private String description;

    private String views;

    private String cover_height;

    private String layout;

    private Images[] images;

    private String datetime;

    private String cover_width;

    private String account_url;

    private String images_count;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getCover_height() {
        return cover_height;
    }

    public void setCover_height(String cover_height) {
        this.cover_height = cover_height;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Images[] getImages() {
        return images;
    }

    public void setImages(Images[] images) {
        this.images = images;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCover_width() {
        return cover_width;
    }

    public void setCover_width(String cover_width) {
        this.cover_width = cover_width;
    }

    public String getAccount_url() {
        return account_url;
    }

    public void setAccount_url(String account_url) {
        this.account_url = account_url;
    }

    public String getImages_count() {
        return images_count;
    }

    public void setImages_count(String images_count) {
        this.images_count = images_count;
    }

    @Override
    public String toString() {
        return "ClassPojo [account_id = " + account_id + ", link = " + link + ", privacy = " + privacy + ", favorite = " + favorite + ", section = " + section + ", id = " + id + ", title = " + title + ", cover = " + cover + ", nsfw = " + nsfw + ", description = " + description + ", views = " + views + ", cover_height = " + cover_height + ", layout = " + layout + ", images = " + images + ", datetime = " + datetime + ", cover_width = " + cover_width + ", account_url = " + account_url + ", images_count = " + images_count + "]";
    }
}
