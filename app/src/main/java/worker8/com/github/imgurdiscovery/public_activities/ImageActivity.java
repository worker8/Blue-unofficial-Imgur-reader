package worker8.com.github.imgurdiscovery.public_activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import worker8.com.github.imgurdiscovery.R;
import worker8.com.github.imgurdiscovery.util.ActionUtil;
import worker8.com.github.imgurdiscovery.util.Util;

public class ImageActivity extends AppCompatActivity {
    @Bind(R.id.image_viewer_image)
    PhotoView mImageView;
    @Bind(R.id.image_viewer_progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.image_viewer_image_deepzoom)
    SubsamplingScaleImageView mImageViewDeepZoom;

    ImageActivity mActivity;

    public final static String ORIGINAL_LINK = "ORIGINAL_LINK";
    //    String mOriginalLink;
    String mUrl;
    Bitmap mDownloadedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_image_viewer);
        ButterKnife.bind(this);
        mUrl = getIntent().getDataString();
//        mOriginalLink = getIntent().getStringExtra(ORIGINAL_LINK);

        Log.d("ddw", "ImageLoader.getInstance(): " + ImageLoader.getInstance());
        Log.d("ddw", "[ImageActivity] getIntent().getDataString(): " + mUrl);

        ImageLoader.getInstance().loadImage(mUrl, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                mDownloadedBitmap = bitmap;
                mImageView.setImageBitmap(bitmap);
                // progress bar
                mProgressBar.setVisibility(View.GONE);
            }
        });

        mImageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                mActivity.finish();
            }
        });

    }

    public void imageViewerOnClick(View view) {
        finish();
    }

//    public void onOpenInBrowserClicked(View button) {
//        ActionUtil.openInBrowser(mActivity, mUrl);
//    }

//    public void onCopyClicked(View button) {
//        ActionUtil.copyText(mActivity, mOriginalLink);
//        Toast.makeText(mActivity, "Copied!", Toast.LENGTH_SHORT).show();
//    }

//    public void onShareClicked(View button) {
//        if (mOriginalLink != null) {
//            ActionUtil.shareUrl(mActivity, mOriginalLink);
//        } else {
//            ActionUtil.shareUrl(mActivity, mUrl);
//        }
//
//    }

    public void onDownloadClicked(View button) {
        ActionUtil.downloadImage(this, mUrl);
    }

    public void onHDClicked(final View button) {
        String linkToBeLoaded;
        if (mUrl != null) {
            linkToBeLoaded = mUrl;
        } else {
            linkToBeLoaded = mUrl;
        }
//            Intent webviewActivityIntent = new Intent(mActivity, WebViewActivity.class);
//            webviewActivityIntent.putExtra(WebViewActivity.WEBVIEW_URL, mOriginalLink);
//            mActivity.startActivity(webviewActivityIntent);
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                        .imageScaleType(ImageScaleType.NONE)
                        .build();
                ImageLoader.getInstance().loadImage(linkToBeLoaded, displayImageOptions, new SimpleImageLoadingListener() {
                    private void toggleVisibility() {
                        mImageView.setVisibility(View.GONE);
                        mImageViewDeepZoom.setVisibility(View.VISIBLE);
                        mImageViewDeepZoom.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                mActivity.finish();
                            }
                        });
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                        String path = Util.writeBitmapToFile(mActivity, "tempDeepZoomImage", bitmap);
                        Log.d("ddw", "onHDClicked absolute Path:" + path);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toggleVisibility();
                                mProgressBar.setVisibility(View.GONE);
                                mImageViewDeepZoom.setImage(ImageSource.uri(path));
//                                    ImageViewColorChanger.changeIconColor((ImageView)button, mActivity.getResources().getColor(R.color.colorAccent));
                                ((ImageView) button).setColorFilter(mActivity.getResources().getColor(R.color.colorAccent));
                            }
                        });
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                        toggleVisibility();
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        toggleVisibility();
                    }
                });
            }
        }).start();


//        }
    }
}
