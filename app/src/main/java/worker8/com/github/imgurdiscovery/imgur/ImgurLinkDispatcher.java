package worker8.com.github.imgurdiscovery.imgur;

import worker8.com.github.jimgur.imgur.paging_api.Data;

public class ImgurLinkDispatcher {
    public enum Type {
        GIF, GIFV, ALBUM, GALLERY, IMAGE, MP4, WEBM
    }

    public static Type getType(Data imgurData) {
        if (imgurData.getType() != null
                && (imgurData.getType().equalsIgnoreCase("image/jpeg")
                || imgurData.getType().equalsIgnoreCase("image/jpg")
                || imgurData.getType().equalsIgnoreCase("image/png"))
                ) {
            return Type.IMAGE;
        } else if (imgurData.getAnimated() != null && imgurData.getAnimated().equalsIgnoreCase("true")) {
            // just use mp4 for now because gif is too huge, gifv's spec is unknown, and VideoView can play mp4 out of the box
            return Type.MP4;
        } else if (imgurData.getIs_album() != null && imgurData.getIs_album().equalsIgnoreCase("true")) {
            return Type.ALBUM;
        }
        return Type.IMAGE;
    }

    public static String getImageLinkFromAlbumCover(String coverId) {
        return "http://i.imgur.com/" + coverId + ".png";
    }
}
