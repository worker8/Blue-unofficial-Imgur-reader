package worker8.com.github.imgurdiscovery.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import worker8.com.github.imgurdiscovery.R;
import worker8.com.github.imgurdiscovery.imgur.ImgurLinkDispatcher;
import worker8.com.github.imgurdiscovery.public_activities.GifActivity;
import worker8.com.github.imgurdiscovery.public_activities.ImageActivity;
import worker8.com.github.imgurdiscovery.util.Util;
import worker8.com.github.jimgur.imgur.paging_api.Data;

public class ImageAdapter extends BaseAdapter {
    MainActivity activity;
    LayoutInflater layoutInflater;
    ArrayList<Data> imgurDataList;

    public ImageAdapter(MainActivity activity, ArrayList<Data> imgurDataList) {
        this.activity = activity;
        this.imgurDataList = imgurDataList;
        layoutInflater = activity.getLayoutInflater();
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
//        Log.d("ImageAdapter", "getType(" + position + "): " + imgurData);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_image, null);
        }
        TextView textView = ButterKnife.findById(convertView, R.id.row_main_tv_title);
        final ImageView imageView = ButterKnife.findById(convertView, R.id.row_main_iv_image);

        clearConvertView(textView, imageView);

        textView.setText(imgurData.getTitle());
//        switch ()
        if (ImgurLinkDispatcher.getType(imgurData) == ImgurLinkDispatcher.Type.MP4) {
            Log.d("ddw", imgurData.getTitle() + " : mp4");
            handleGif(imgurData, imageView);
        } else if (ImgurLinkDispatcher.getType(imgurData) == ImgurLinkDispatcher.Type.IMAGE) {
            Log.d("ddw", imgurData.getTitle() + " : image");
            handleImage(imgurData, imageView);
        } else if (ImgurLinkDispatcher.getType(imgurData) == ImgurLinkDispatcher.Type.ALBUM) {
            Log.d("ddw", imgurData.getTitle() + " : album");
            handleAlbum(imgurData, imageView);
        }

        return convertView;
    }

    private void handleAlbum(Data imgurData, ImageView imageView) {
        String coverLink = ImgurLinkDispatcher.getImageLinkFromAlbumCover(imgurData.getCover());
        Log.d("ImageAdapter", "handleAlbum() called with: " + "cover link = [" + coverLink + "], imageView = [" + imageView + "]");
        ImageLoader.getInstance().loadImage(coverLink, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                int calculatedHeight = (int) ((float) bitmap.getHeight() / (float) bitmap.getWidth() * imageView.getWidth());

                // image
                imageView.getLayoutParams().height = calculatedHeight;
                imageView.setImageBitmap(bitmap);
                // TODO: hide the progress bar

            }
        });
    }

    private void handleImage(Data imgurData, ImageView imageView) {
        ImageLoader.getInstance().loadImage(imgurData.getLink(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                int calculatedHeight = (int) ((float) bitmap.getHeight() / (float) bitmap.getWidth() * imageView.getWidth());

                // image
                imageView.getLayoutParams().height = calculatedHeight;
                imageView.setImageBitmap(bitmap);
                // TODO: hide the progress bar

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ImageAdapter", "image onClick: ");
                Intent intent = new Intent(activity, ImageActivity.class);
                intent.setData(Uri.parse(imgurData.getLink()));
                activity.startActivity(intent);
            }
        });
    }

    private void handleGif(Data imgurData, ImageView imageView) {
        final String thumbnail = imgurData.getLink().replace(".mp4", "l.png");

        ImageLoader.getInstance().loadImage(thumbnail, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                int calculatedHeight = (int) ((float) bitmap.getHeight() / (float) bitmap.getWidth() * imageView.getWidth());

                // image
                imageView.getLayoutParams().height = calculatedHeight;
                imageView.setImageBitmap(bitmap);
                // TODO: hide the progress bar

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gifActivityIntent = new Intent(activity, GifActivity.class);
                gifActivityIntent.putExtra(GifActivity.GIF_URL, imgurData.getMp4());
                activity.startActivity(gifActivityIntent);
            }
        });
    }

    private void clearConvertView(TextView textView, ImageView imageView) {
        textView.setText("");
        imageView.getLayoutParams().height = (int) Util.convertDpToPixel(150, activity);
        imageView.setImageResource(android.R.color.transparent);
    }
}
