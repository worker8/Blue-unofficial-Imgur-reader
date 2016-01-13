package worker8.com.github.imgurdiscovery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
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

        TextView textView;
        ImageView imageView;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_image, null);
        }
        textView = ButterKnife.findById(convertView, R.id.row_main_tv_title);
        imageView = ButterKnife.findById(convertView, R.id.row_main_iv_image);

        // TODO: clear convertView so that a recycled convertView doesn't show data from previous post
        clearConvertView(convertView);

        textView.setText(imgurData.getTitle());

        return convertView;
    }

    private void clearConvertView(View convertView) {

    }
}
