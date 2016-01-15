package worker8.com.github.imgurdiscovery.main;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import main.java.com.github.worker8.HtmlFormatter;
import worker8.com.github.imgurdiscovery.R;
import worker8.com.github.imgurdiscovery.imgur.ImgurLinkDispatcher;
import worker8.com.github.imgurdiscovery.imgur_album.ImgurAlbumActivity;
import worker8.com.github.imgurdiscovery.public_activities.GifActivity;
import worker8.com.github.imgurdiscovery.public_activities.ImageActivity;
import worker8.com.github.imgurdiscovery.util.Constant;
import worker8.com.github.imgurdiscovery.util.Util;
import worker8.com.github.jimgur.imgur.paging_api.Data;

public class ImageAdapter extends BaseAdapter {
    MainActivity activity;
    LayoutInflater layoutInflater;
    ArrayList<Data> imgurDataList;

    public static final int ImagePlaceHolderHeight_dp = 200;

    public ImageAdapter(MainActivity activity, ArrayList<Data> imgurDataList) {
        this.activity = activity;
        this.imgurDataList = imgurDataList;
        layoutInflater = activity.getLayoutInflater();
    }

    public void addNewData(Data[] newDataList) {
        if (newDataList != null && newDataList.length > 0) {
            for (int i = 0; i < newDataList.length; i++) {
                imgurDataList.add(newDataList[i]);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (imgurDataList != null) {
            return imgurDataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Data imgurData = imgurDataList.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_image, null);
        }
        /* find views + clearing them */
        TextView titleTV = ButterKnife.findById(convertView, R.id.row_main_tv_title);
        TextView authorTV = ButterKnife.findById(convertView, R.id.row_main_tv_author);
        LinearLayout container = ButterKnife.findById(convertView, R.id.card_image_container);
        container.setLayoutTransition(new LayoutTransition());
        final ImageView imageView = ButterKnife.findById(convertView, R.id.row_main_iv_image);
        final TextView tagTV = ButterKnife.findById(convertView, R.id.row_main_tv_tag);

        ProgressBar progressBar = ButterKnife.findById(convertView, R.id.card_image_pb_loading);

        clearView(titleTV, tagTV, authorTV, imageView, progressBar);

        String titleText = HtmlFormatter.from(imgurData.getAccount_url()).fontColor("#01579B").bold().getHtmlString();
        long time = Long.parseLong(imgurData.getDatetime()) * 1000;

        String creationDateFormatted = DateUtils.getRelativeTimeSpanString(time,
                new Date().getTime(), DateUtils.FORMAT_ABBREV_RELATIVE).toString().toLowerCase();
        titleText += HtmlFormatter.from(Constant.MIDDLE_DOT + creationDateFormatted).small().getHtmlString();
        authorTV.setText(Html.fromHtml(titleText), TextView.BufferType.SPANNABLE);
        titleTV.setText(imgurData.getTitle());
        if (ImgurLinkDispatcher.getType(imgurData) == ImgurLinkDispatcher.Type.MP4) {
            handleGif(imgurData, imageView, progressBar);
            tagTV.setText(R.string.animated);
            tagTV.setVisibility(View.VISIBLE);
        } else if (ImgurLinkDispatcher.getType(imgurData) == ImgurLinkDispatcher.Type.IMAGE) {
            handleImage(imgurData, imageView, progressBar);
            tagTV.setVisibility(View.GONE);
        } else if (ImgurLinkDispatcher.getType(imgurData) == ImgurLinkDispatcher.Type.ALBUM) {
            handleAlbum(imgurData, imageView, progressBar);
            tagTV.setText(R.string.album);
            tagTV.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private void handleAlbum(Data imgurData, ImageView imageView, ProgressBar progressBar) {
        String coverLink = ImgurLinkDispatcher.getImageLinkFromAlbumCover(imgurData.getCover());
        ImageLoader.getInstance().loadImage(coverLink, new CardImageLoadingListener(imageView, progressBar));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ImgurAlbumActivity.class);
                intent.setData(Uri.parse(imgurData.getId()));
                activity.startActivity(intent);
            }
        });
    }

    private void handleImage(Data imgurData, ImageView imageView, ProgressBar progressBar) {
        ImageLoader.getInstance().loadImage(imgurData.getLink(), new CardImageLoadingListener(imageView, progressBar));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ImageActivity.class);
                intent.setData(Uri.parse(imgurData.getLink()));
                activity.startActivity(intent);
            }
        });
    }

    private void handleGif(Data imgurData, ImageView imageView, ProgressBar progressBar) {
        final String thumbnail = imgurData.getLink().replace(".mp4", "l.png");

        ImageLoader.getInstance().loadImage(thumbnail, new CardImageLoadingListener(imageView, progressBar));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gifActivityIntent = new Intent(activity, GifActivity.class);
                gifActivityIntent.putExtra(GifActivity.GIF_URL, imgurData.getMp4());
                activity.startActivity(gifActivityIntent);
            }
        });
    }

    private void clearView(TextView titleTV, TextView tagTV, TextView authorTV, ImageView imageView, ProgressBar progressBar) {
        titleTV.setText("");
        authorTV.setText("");
        tagTV.setText("");
        tagTV.setVisibility(View.GONE);
        imageView.getLayoutParams().height = (int) Util.convertDpToPixel(ImagePlaceHolderHeight_dp, activity);
        imageView.setImageResource(android.R.color.transparent);
        imageView.setOnClickListener(null);
        progressBar.setVisibility(View.VISIBLE);
    }

    public class CardImageLoadingListener extends SimpleImageLoadingListener {
        ImageView imageView;
        ProgressBar progressBar;

        public CardImageLoadingListener(ImageView imageView, ProgressBar progressBar) {
            this.imageView = imageView;
            this.progressBar = progressBar;
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
            int calculatedHeight = (int) ((float) bitmap.getHeight() / (float) bitmap.getWidth() * imageView.getWidth());

            // image
            imageView.getLayoutParams().height = calculatedHeight;
            imageView.setImageBitmap(bitmap);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            super.onLoadingCancelled(imageUri, view);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            super.onLoadingFailed(imageUri, view, failReason);
            progressBar.setVisibility(View.GONE);
        }
    }
}
