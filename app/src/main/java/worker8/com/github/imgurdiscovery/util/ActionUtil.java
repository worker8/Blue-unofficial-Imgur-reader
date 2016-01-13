package worker8.com.github.imgurdiscovery.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import worker8.com.github.imgurdiscovery.R;

public class ActionUtil {

    public static void shareUrl(Activity activity, String url) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain"); //NON-NLS
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url);
        activity.startActivity(Intent.createChooser(sharingIntent, activity.getString(R.string.share_via)));
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
}
