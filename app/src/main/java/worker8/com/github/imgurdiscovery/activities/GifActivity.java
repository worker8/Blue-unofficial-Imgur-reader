package worker8.com.github.imgurdiscovery.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import worker8.com.github.imgurdiscovery.R;
import worker8.com.github.imgurdiscovery.custom_view.MediaControllerWithCallback;
import worker8.com.github.imgurdiscovery.util.Util;

/**
 * This activity is responsible of handling all animated type of images in imgur.
 * Imgur animated images can come in a few types of extension: .gif .gifv .webm .mp4
 * Since Android's {@link VideoView} is able to play .mp4 out of the box,
 * .gif .gifv .webm are all turned into .mp4 and being rendered as .mp4
 */
public class GifActivity extends AppCompatActivity {
    public final static String GIF_URL = "GIF_URL";

    @Bind(R.id.gif_image)
    SimpleDraweeView gifImage;

    @Bind(R.id.video_view)
    VideoView videoView;

    @Bind(R.id.gif_close_button)
    FloatingActionButton closeButton;

    Activity activity;
    String mGifUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_gif);
        ButterKnife.bind(this);

        mGifUrl = getIntent().getStringExtra(GIF_URL);
        if (mGifUrl == null || mGifUrl.equals("")) {
            Toast.makeText(GifActivity.this, R.string.invalid_gif_link, Toast.LENGTH_SHORT).show();
            finish();
        }
        Log.d("GifActivity", "onCreate: mGifUrl = " + mGifUrl);
        if (mGifUrl != null && mGifUrl.length() > 0) {
            // links that are passed in are assumed to be .mp4, otherwise, do not feed into VideoView
            if (mGifUrl.endsWith(".mp4")) {
                gifImage.setVisibility(View.GONE);
                Uri uri = Uri.parse(mGifUrl); //Declare your id here.
                final MediaControllerWithCallback mediaController = new MediaControllerWithCallback(this);
                mediaController.mOnShowListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeButton.setVisibility(View.VISIBLE);
                    }
                };
                mediaController.mOnHideListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeButton.setVisibility(View.GONE);
                    }
                };
                videoView.setZOrderOnTop(true);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                    ((ViewGroup) closeButton.getParent()).removeView(closeButton);
                                    videoView.setZOrderMediaOverlay(false);
                                    ((FrameLayout) mediaController.getParent()).addView(closeButton);
                                    closeButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                    int margin = (int) Util.convertDpToPixel(10, activity);
                                    ((FrameLayout.LayoutParams) closeButton.getLayoutParams()).setMargins(0, margin, margin, 0);
                                }
                                return false;
                            }
                        });
                    }

                });
                mediaController.setAnchorView(videoView);

                videoView.setMediaController(mediaController);

                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.start();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.start();
        }

    }

    public void onCloseButtonClicked(View view) {
        finish();
    }
}
