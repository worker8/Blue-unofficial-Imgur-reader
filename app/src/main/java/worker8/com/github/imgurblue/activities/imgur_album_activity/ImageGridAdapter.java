package worker8.com.github.imgurblue.activities.imgur_album_activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import worker8.com.github.imgurblue.R;
import worker8.com.github.imgurblue.custom_view.SquareImageView;
import worker8.com.github.imgurblue.imgur_wrapper.ImgurUtil;
import worker8.com.github.jimgur.imgur.album_api.AlbumResponse;
import worker8.com.github.jimgur.imgur.gallery_api.GalleryResponse;


public class ImageGridAdapter extends BaseAdapter {

    private Context mContext;
    private Object mImgurData;

    public ImageGridAdapter(Context c, Object imgurData) {
        mContext = c;
        mImgurData = imgurData;
    }

    public int getCount() {
        if (mImgurData instanceof AlbumResponse){
            return ((AlbumResponse)mImgurData).getData().getImages().length;
        } else {
            return ((GalleryResponse)mImgurData).getData().getImages().length;
        }
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SquareImageView imageView;
        FrameLayout frameLayout;

        /* init new ones, or use recycle ones */
        if (convertView == null) {
            imageView = new SquareImageView(mContext);
            frameLayout = new FrameLayout(mContext);
            frameLayout.addView(imageView, 0);
        } else {
            frameLayout = (FrameLayout)convertView;
            imageView = (SquareImageView) frameLayout.getChildAt(0);
        }

        /* setup frame layout */
        frameLayout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        frameLayout.setForeground(mContext.getResources().getDrawable(R.drawable.touch_feedback_white));
        /* setup image view */
        imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 0)); // width is 0 becaue SquareImageView is used, width will be ignored anyway
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //set grid background to grey, this background will be replaced by the thumbnails once thumbnails are loaded
        imageView.setImageResource(android.R.color.transparent);
        imageView.setBackgroundColor(mContext.getResources().getColor(R.color.md_grey_800));
        /* load the image */
        if (mImgurData instanceof AlbumResponse){
            ImageLoader.getInstance().displayImage(ImgurUtil.getBigSquareImgurLink(((AlbumResponse)mImgurData).getData().getImages()[position].getLink()), imageView);
        } else {
            ImageLoader.getInstance().displayImage(ImgurUtil.getBigSquareImgurLink(((GalleryResponse)mImgurData).getData().getImages()[position].getLink()), imageView);
        }


        return frameLayout;
    }
}
