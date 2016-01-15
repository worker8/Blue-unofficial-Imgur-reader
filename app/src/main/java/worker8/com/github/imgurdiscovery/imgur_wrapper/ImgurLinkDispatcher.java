package worker8.com.github.imgurdiscovery.imgur_wrapper;

import worker8.com.github.jimgur.imgur.paging_api.Data;

/**
 * <pre>
 * Imgur posts come in various different format, it can mainly be broken down into 3 categories:
 * - normal image (.png .jpeg)
 * - animated image (.gif .gifv .webm .mp4)
 * - album (it contains a link to multiple images)
 *
 * This class is used to detect and dispatch the work to appropriate activities
 *
 * Activities vs type of categories it handles:
 *
 * - normal image is handled by {@link worker8.com.github.imgurdiscovery.activities.ImageViewerActivity ImageViewerActivity}
 * - animated image is handled by {@link worker8.com.github.imgurdiscovery.activities.GifActivity GifActivity}
 * - album is handled by {@link worker8.com.github.imgurdiscovery.activities.imgur_album_activity.ImgurAlbumActivity ImgurAlbumActivity}
 * </pre>
 */
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
