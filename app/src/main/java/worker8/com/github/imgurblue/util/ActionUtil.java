package worker8.com.github.imgurblue.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import worker8.com.github.imgurblue.R;

/**
 * A class that provides static reusable methods to accomplish commonly used actions
 * such as: sharing, download an image, open a link in browser, copy some text, etc
 */
public class ActionUtil {

    public static void shareUrl(Activity activity, String url) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain"); //NON-NLS
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url);
        activity.startActivity(Intent.createChooser(sharingIntent, activity.getString(R.string.share_via)));
    }

    public static void copyText(Context context, String textToBeCopied) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(textToBeCopied);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("", textToBeCopied);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static void downloadImage(Activity activity, String url) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Imgur Discovery");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Download Image")
                .setDescription("Download from Imgur Discovery.")
                .setDestinationInExternalPublicDir("/Imgur Discovery", "image.jpg");

        mgr.enqueue(request);

    }

    public static void openInBrowser(Activity activity, String link) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_VIEW);
        sharingIntent.setData(Uri.parse(link));
        activity.startActivity(sharingIntent);
    }
}
