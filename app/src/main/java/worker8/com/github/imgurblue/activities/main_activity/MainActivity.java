package worker8.com.github.imgurblue.activities.main_activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;
import main.java.com.github.worker8.HtmlFormatter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import worker8.com.github.imgurblue.R;
import worker8.com.github.imgurblue.imgur_wrapper.ImgurConstant;
import worker8.com.github.imgurblue.pref.GeneralPref;
import worker8.com.github.imgurblue.util.RxUtils;
import worker8.com.github.imgurblue.util.Util;
import worker8.com.github.jimgur.imgur.paging_api.ImgurPaginationResponse;
import worker8.com.github.jimgur.imgur.paging_api.ImgurPaginator;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = "MainActivity";
    MainActivity activity;

    /* data models */
    private CompositeSubscription _subscriptions = new CompositeSubscription();
    private ImgurPaginator imgurPaginator;
    ImageAdapter imageAdapter;
    /**
     * currently selected section, changeable from nav drawer
     */
    String currentSection;

    /* UI variables */
    @Bind(R.id.main_cl)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.main_lv_image_list)
    ListView imageListView;
    @Bind(R.id.main_pb_loading)
    ProgressBar progressBarMiddle; // this is used when the page is empty
    @Bind(R.id.main_pb_bottom_load_more)
    ProgressBar progressBarBottom; // this is used when the page is not empty, and when it is loading more
    @Bind(R.id.main_fab)
    FloatingActionButton fab;
    // nav area
    @Bind(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_lv)
    ListView navListView;
    // toolbar
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_toolbar_tv_title)
    TextView toolbarTitleTV;
    @Bind(R.id.main_toolbar_tv_section)
    TextView toolbarSectionTV;

    /**
     * This variable is used to serve as a simple lock to prevent making multiple calls to load more content when we hit the bottom of the list
     */
    boolean loadMoreLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        activity = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        setSupportActionBar(toolbar);

        /* setup burger menu */
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open_string, R.string.drawer_close_string);
        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawerToggle.syncState();

        navListView.setAdapter(new NavAdapter(activity));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int randomSectionPosition = Util.randInt(0, ImgurConstant.INSTANCE.getSectionList().size() - 1);
                String section = ImgurConstant.INSTANCE.getSectionList().get(randomSectionPosition);
                EventBus.getDefault().post(new MainActivity.NewSectionSelectedEvent(section));
            }
        });
        currentSection = GeneralPref.get(this).getLastVisitedSub();
        toolbarSectionTV.setText(currentSection);
        loadMore(currentSection);
    }

    /**
     * This method will clear off {@link MainActivity#imgurPaginator}, {@link MainActivity#imageAdapter}
     */
    private void reset() {
        imgurPaginator = null;
        imageAdapter = null;
        imageListView.setAdapter(null);
    }

    Snackbar connectionSnackbar;

    /**
     * This is the most interesting method in this activity.
     * It loads more content
     * @param section imgur has different 'section', example input "hot", "top", "r/aww", "r/pics"
     */
    private void loadMore(String section) {
        // do not proceed without internet connection
        if (!Util.isNetworkAvailable(this)) {
            connectionSnackbar = Snackbar.make(coordinatorLayout, "Please check your internet connection, and hit refresh...", Snackbar.LENGTH_INDEFINITE);
            View sbView = connectionSnackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryBlue));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            connectionSnackbar.show();
            progressBarMiddle.setVisibility(View.GONE);
            progressBarBottom.setVisibility(View.GONE);
            return;
        } else {
            if (connectionSnackbar != null && connectionSnackbar.isShown()) {
                connectionSnackbar.dismiss();
            }
        }

        if (imgurPaginator == null) {
            // this will run when this activity is launched for the 1st time (when the page is blank)
            imgurPaginator = new ImgurPaginator(section);
            progressBarMiddle.setVisibility(View.VISIBLE);
            progressBarBottom.setVisibility(View.GONE); // don't show for the 1st time
        } else {
            // subsequent call to load more will enter the else block
            progressBarMiddle.setVisibility(View.GONE);
            progressBarBottom.setVisibility(View.VISIBLE);
        }
        if (!loadMoreLock) {
            loadMoreLock = true;
            _subscriptions.add(Observable.just(1)
                    .map(i -> imgurPaginator.next())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ImgurPaginationResponse>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted: ");
                            progressBarMiddle.setVisibility(View.GONE);
                            progressBarBottom.setVisibility(View.GONE);
                            loadMoreLock = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: ");
                            progressBarMiddle.setVisibility(View.GONE);
                            progressBarBottom.setVisibility(View.GONE);
                            loadMoreLock = false;
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(ImgurPaginationResponse imgurPaginationResponse) {
                            Log.d(TAG, "onNext: ");
                            progressBarMiddle.setVisibility(View.GONE);
                            progressBarBottom.setVisibility(View.GONE);
                            onNewDataLoaded(imgurPaginationResponse);
                        }
                    }));
        }
    }

    /**
     * This is called when {@link #loadMore(String)} obtained more data
     *
     * @param imgurPaginationResponse data that is loaded by {@link #loadMore(String)}
     */
    private void onNewDataLoaded(ImgurPaginationResponse imgurPaginationResponse) {
        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter(activity, imgurPaginationResponse.getImgurDataList());
            imageListView.setAdapter(imageAdapter);
            imageListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    final int lastItem = firstVisibleItem + visibleItemCount;
                    if (lastItem == totalItemCount) {
                        if (!loadMoreLock) { //to avoid multiple calls for last item
                            loadMore(currentSection);
                        }
                    }
                }
            });
        }
        imageAdapter.addNewData(imgurPaginationResponse.getData());
        loadMoreLock = false;
    }

    /**
     * This is triggered when a new section in the navigational drawer is clicked
     */
    public void onEvent(NewSectionSelectedEvent event) {
        currentSection = event.section;
        GeneralPref.get(this).setLastVisitedSub(currentSection);
        toolbarSectionTV.setText(currentSection);
        reset();
        loadMore(currentSection);

        // show snackbar
        String pointString = HtmlFormatter.from(currentSection).bold().getHtmlString();
        pointString += getString(R.string.is_selected);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, pointString, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryBlue));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setText(Html.fromHtml(textView.getText().toString()));
        snackbar.show();
        drawerLayout.closeDrawers();
    }

    public static class NewSectionSelectedEvent {
        public String section;

        public NewSectionSelectedEvent(String section) {
            this.section = section;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(_subscriptions);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            reset();
            loadMore(currentSection);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
