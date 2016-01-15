package worker8.com.github.imgurdiscovery.imgur;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class ImgurUtil {
    private static final String REGEX_IMGUR_IMAGE_URL_QUERY = "^([hH][tT][tT][pP]|[hH][tT][tT][pP][sS]):\\/\\/" +
            "(m.imgur.com|imgur.com|i.imgur.com)\\/(?!=\\/)(\\w+)(.jpg|.jpeg|.png)\\?\\w+$";
    private static final Pattern PATTERN_IMGUR_IMAGE_URL_QUERY = Pattern.compile(REGEX_IMGUR_IMAGE_URL_QUERY);

    /* direct imgur image id */
    private static final String REGEX_IMGUR_DIRECT_LINK = "^([hH][tT][tT][pP]|[hH][tT][tT][pP][sS]):\\/\\/" +
            "(m.imgur.com|imgur.com|i.imgur.com)\\/(?!=\\/)(\\w+)(.jpg|.jpeg|.png)$";
    private static final Pattern PATTERN_IMGUR_DIRECT_LINK = Pattern.compile(REGEX_IMGUR_DIRECT_LINK);

    //https://api.imgur.com/models/image
    //Thumbnail suffix 'b' means Big Square with 160x160
    public static String getBigSquareImgurLink(String imgurLink) {
        String bigSquareImgurLink = String.copyValueOf(imgurLink.toCharArray());
        LinkMatch linkMatch = findLinkMatch(bigSquareImgurLink);
        if (linkMatch == LinkMatch.IMGUR_DIRECT_LINK || linkMatch == LinkMatch.IMGUR_IMAGE_URL_QUERY) {
            bigSquareImgurLink = bigSquareImgurLink.replace(".jpg", "b.jpg");
            bigSquareImgurLink = bigSquareImgurLink.replace(".jpeg", "b.jpeg");
            bigSquareImgurLink = bigSquareImgurLink.replace(".png", "b.png");
            return bigSquareImgurLink;
        } else {
            return imgurLink;
        }
    }

    public enum LinkMatch {
        NONE,
        IMGUR_DIRECT_LINK,
        IMGUR_IMAGE_URL_QUERY,
    }

    /**
     * Find the type of imgur link that belongs to the id
     *
     * @param url
     * @return
     */
    public static LinkMatch findLinkMatch(String url) {
        LinkMatch match = LinkMatch.NONE;

        if (!TextUtils.isEmpty(url)) {
            if (url.matches(REGEX_IMGUR_DIRECT_LINK)) {
                match = LinkMatch.IMGUR_DIRECT_LINK;
            } else if (url.matches(REGEX_IMGUR_IMAGE_URL_QUERY)) {
                match = LinkMatch.IMGUR_IMAGE_URL_QUERY;
            }
        }

        return match;
    }
}
