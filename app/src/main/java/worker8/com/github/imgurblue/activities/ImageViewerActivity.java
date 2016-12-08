package worker8.com.github.imgurblue.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import worker8.com.github.imgurblue.R;
import worker8.com.github.imgurblue.util.ActionUtil;
import worker8.com.github.imgurblue.util.Util;

/**
 * Activity that handles non-animated image from imgur, such as .png .jpeg
 */
public class ImageViewerActivity extends AppCompatActivity {
    @Bind(R.id.image_viewer_image)
    PhotoView imageView;
    @Bind(R.id.image_viewer_progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.image_viewer_image_deepzoom)
    SubsamplingScaleImageView imageViewDeepZoom;

    ImageViewerActivity activity;
    String mUrl;
    Bitmap mDownloadedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_image_viewer);
        ButterKnife.bind(this);
        mUrl = getIntent().getDataString();

        ImageLoader.getInstance().loadImage(mUrl, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                mDownloadedBitmap = bitmap;
                imageView.setImageBitmap(bitmap);
                // progress bar
                progressBar.setVisibility(View.GONE);
            }
        });

        imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                activity.finish();
            }
        });

    }

    public void imageViewerOnClick(View view) {
        finish();
    }

    public void onOpenInBrowserClicked(View button) {
        ActionUtil.openInBrowser(activity, mUrl);
    }

    public void onCopyClicked(View button) {
        ActionUtil.copyText(activity, mUrl);
        Toast.makeText(activity, activity.getString(R.string.link_is_copied), Toast.LENGTH_SHORT).show();
    }

    public void onShareClicked(View button) {
        ActionUtil.shareUrl(activity, mUrl);
    }

    public void onDownloadClicked(View button) {
        ActionUtil.downloadImage(this, mUrl);
    }

    /**
     * The purpose of this button is to enable deep zooming
     * Some images are really huge, such as images from imgur.com/r/earthporn, it can be 5000x6000 from some super duper camera
     * If we try to load it into a normal {@link ImageView}, we will get out of memory error
     * Luckily, very smart programmer shares an open source library called <a href="https://github.com/davemorrissey/subsampling-scale-image-view">Subsampling Scale Image View</a>
     * {@link SubsamplingScaleImageView} is able to load super large images without error and provides smooth scrolling
     * TODO: this sometimes failed for very long images... need troubleshooting
     * @param button
     * @see <a href="https://github.com/davemorrissey/subsampling-scale-image-view">Subsampling scale image view github page</a>
     */
    public void onHDClicked(final View button) {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                        .imageScaleType(ImageScaleType.NONE)
                        .build();
                ImageLoader.getInstance().loadImage(mUrl, displayImageOptions, new SimpleImageLoadingListener() {
                    private void toggleVisibility() {
                        imageView.setVisibility(View.GONE);
                        imageViewDeepZoom.setVisibility(View.VISIBLE);
                        imageViewDeepZoom.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                activity.finish();
                            }
                        });
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                        String path = Util.writeBitmapToFile(activity, "worker8.com.github.imgurblue-tempDeepZoomImage", bitmap);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageViewDeepZoom.setImage(ImageSource.uri(path));
                                toggleVisibility();

                                imageViewDeepZoom.setOnImageEventListener(new SubsamplingScaleImageView.DefaultOnImageEventListener() {
                                    @Override
                                    public void onImageLoaded() {
                                        progressBar.setVisibility(View.GONE);
                                        ((ImageView) button).setColorFilter(activity.getResources().getColor(R.color.colorPrimaryBlueDark));
                                    }

                                    @Override
                                    public void onPreviewLoadError(Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(activity, getString(R.string.oops_message), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onImageLoadError(Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(activity, getString(R.string.oops_message), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onTileLoadError(Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(activity, getString(R.string.oops_message), Toast.LENGTH_SHORT).show();
                                    }
                                });

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
    }
}
