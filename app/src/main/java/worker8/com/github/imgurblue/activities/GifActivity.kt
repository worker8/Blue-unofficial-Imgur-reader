package worker8.com.github.imgurblue.activities

import android.app.Activity
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import android.widget.VideoView

import butterknife.Bind
import butterknife.ButterKnife
import worker8.com.github.imgurblue.R
import worker8.com.github.imgurblue.custom_view.MediaControllerWithCallback
import worker8.com.github.imgurblue.util.Util

/**
 * This activity is responsible of handling all animated type of images in imgur.
 * Imgur animated images can come in a few types of extension: .gif .gifv .webm .mp4
 * Since Android's [VideoView] is able to play .mp4 out of the box,
 * .gif .gifv .webm are all turned into .mp4 and being rendered as .mp4
 */
class GifActivity : AppCompatActivity() {

    @Bind(R.id.video_view)
    var videoView: VideoView? = null

    @Bind(R.id.gif_close_button)
    var closeButton: FloatingActionButton? = null

    val activity: Activity = this
    var mGifUrl: String? = null

    var tempString : String = "asdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif)
        ButterKnife.bind(this)

        mGifUrl = intent.getStringExtra(GIF_URL)
        if (mGifUrl == null || mGifUrl == "") {
            Toast.makeText(this@GifActivity, R.string.invalid_gif_link, Toast.LENGTH_SHORT).show()
            finish()
        }
        Log.d("GifActivity", "onCreate: mGifUrl = " + mGifUrl!!)
        if (mGifUrl != null && mGifUrl!!.length > 0) {
            // links that are passed in are assumed to be .mp4, otherwise, do not feed into VideoView
            if (mGifUrl!!.endsWith(".mp4")) {
                val uri = Uri.parse(mGifUrl) //Declare your id here.
                val mediaController = MediaControllerWithCallback(this)
                mediaController.mOnShowListener = View.OnClickListener { closeButton!!.visibility = View.VISIBLE }
                mediaController.mOnHideListener = View.OnClickListener { closeButton!!.visibility = View.GONE }
                videoView!!.setZOrderOnTop(true)
                videoView!!.setOnPreparedListener { mp ->
                    mp.isLooping = true
                    mp.setOnInfoListener { mp, what, extra ->
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            (closeButton!!.parent as ViewGroup).removeView(closeButton)
                            videoView!!.setZOrderMediaOverlay(false)
                            (mediaController.parent as FrameLayout).addView(closeButton)
                            closeButton!!.setBackgroundColor(resources.getColor(android.R.color.transparent))
                            val margin = Util.convertDpToPixel(10f, activity).toInt()
                            (closeButton!!.layoutParams as FrameLayout.LayoutParams).setMargins(0, margin, margin, 0)
                        }
                        false
                    }
                }
                mediaController.setAnchorView(videoView)

                videoView!!.setMediaController(mediaController)

                videoView!!.setVideoURI(uri)
                videoView!!.requestFocus()
                videoView!!.start()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (videoView != null) {
            videoView!!.start()
        }

    }

    fun onCloseButtonClicked(view: View) {
        finish()
    }

    companion object {
        @JvmField
        val GIF_URL = "GIF_URL"
    }
}
