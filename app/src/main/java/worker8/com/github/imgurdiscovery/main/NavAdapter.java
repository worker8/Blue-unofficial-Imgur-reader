package worker8.com.github.imgurdiscovery.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import worker8.com.github.imgurdiscovery.R;
import worker8.com.github.imgurdiscovery.imgur.ImgurConstant;

public class NavAdapter extends BaseAdapter {
    MainActivity activity;

    public NavAdapter(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return ImgurConstant.sectionList.size();
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
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.nav_row, null);
        }
        TextView navRowTV = ButterKnife.findById(convertView, R.id.nav_row_tv);
        clearView(navRowTV);

        String section = ImgurConstant.sectionList.get(position);
        navRowTV.setText(section);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MainActivity.NewSectionSelectedEvent(section));
            }
        });
        return convertView;
    }

    public void clearView(TextView navRowTV) {
        navRowTV.setText("");
        navRowTV.setOnClickListener(null);
    }
}
