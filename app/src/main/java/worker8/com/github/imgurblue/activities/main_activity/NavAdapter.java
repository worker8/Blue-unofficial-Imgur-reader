package worker8.com.github.imgurblue.activities.main_activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import worker8.com.github.imgurblue.R;
import worker8.com.github.imgurblue.imgur_wrapper.ImgurConstant;

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
            convertView = activity.getLayoutInflater().inflate(R.layout.row_nav_drawer, null);
        }
        TextView navRowTV = ButterKnife.findById(convertView, R.id.nav_row_tv);
        clearView(navRowTV);

        String section = ImgurConstant.sectionList.get(position);
        navRowTV.setText(section);
        convertView.setOnClickListener(v -> EventBus.getDefault().post(new MainActivity.NewSectionSelectedEvent(section)));
        return convertView;
    }

    public void clearView(TextView navRowTV) {
        navRowTV.setText("");
        navRowTV.setOnClickListener(null);
    }
}
