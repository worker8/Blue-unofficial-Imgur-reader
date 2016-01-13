package worker8.com.github.imgurdiscovery.public_activities;

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
import worker8.com.github.imgurdiscovery.util.Util;

public class GifActivity extends AppCompatActivity {
    public final static String GIF_URL = "GIF_URL";

    @Bind(R.id.gif_image)
    SimpleDraweeView mGifImage;

    @Bind(R.id.video_view)
    VideoView mVideoView;

    @Bind(R.id.gif_close_button)
    FloatingActionButton mCloseButton;

    Activity mActivity;
//    @Bind(R.id.media_controller)
//    MediaController mMediaController;

    String mGifUrl;

    //    TODO: messy code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_gif);
        ButterKnife.bind(this);

        mGifUrl = getIntent().getStringExtra(GIF_URL);
        if (mGifUrl == null || mGifUrl.equals("")) {
            Toast.makeText(GifActivity.this, R.string.invalid_gif_link, Toast.LENGTH_SHORT).show();
            finish();
        }
        Log.d("gifactivity", "onCreate: mGifUrl = " + mGifUrl);
        if (mGifUrl != null && mGifUrl.length() > 0) {
            if (mGifUrl.endsWith(".mp4")) {
                mGifImage.setVisibility(View.GONE);
                Uri uri = Uri.parse(mGifUrl); //Declare your url here.
                final MediaControllerWithCallback mediaController = new MediaControllerWithCallback(this);
                mediaController.mOnShowListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCloseButton.setVisibility(View.VISIBLE);
                    }
                };
                mediaController.mOnHideListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCloseButton.setVisibility(View.GONE);
                    }
                };
                mVideoView.setZOrderOnTop(true);
                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.d("ddw", "onPrepared");
                        mp.setLooping(true);
                        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                    ((ViewGroup) mCloseButton.getParent()).removeView(mCloseButton);
                                    mVideoView.setZOrderMediaOverlay(false);
                                    ((FrameLayout) mediaController.getParent()).addView(mCloseButton);
                                    mCloseButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                    int margin = (int) Util.convertDpToPixel(10, mActivity);
                                    ((FrameLayout.LayoutParams) mCloseButton.getLayoutParams()).setMargins(0, margin, margin, 0);
                                }
                                return false;
                            }
                        });
//                    mVideoView.setBackgroundColor(android.R.color.transparent);
//                        mVideoView.setVisibility(View.VISIBLE);
//                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//                        @Override
//                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        //                        // add media controller
                        //                        mVideoView.setMediaController(mediaController);
                        //                        // and set its position on screen
                        //                        mediaController.setAnchorView(mVideoView);
//                        }
//                    });
                    }

                });
                mediaController.setAnchorView(mVideoView);

                mVideoView.setMediaController(mediaController);

                mVideoView.setVideoURI(uri);
                mVideoView.requestFocus();
                mVideoView.start();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.start();
        }

    }

    public void onCloseButtonClicked(View view) {
        finish();
    }
}
