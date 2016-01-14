package worker8.com.github.imgurdiscovery.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import worker8.com.github.imgurdiscovery.R;
import worker8.com.github.imgurdiscovery.imgur.ImgurConstant;
import worker8.com.github.imgurdiscovery.util.RxUtils;
import worker8.com.github.imgurdiscovery.util.Util;
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
    @Bind(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_lv)
    ListView navListView;
    @Bind(R.id.main_lv_image_list)
    ListView imageListView;
    @Bind(R.id.main_pb_loading)
    ProgressBar progressBarMiddle; // this is used when the page is empty
    @Bind(R.id.main_pb_bottom_load_more)
    ProgressBar progressBarBottom; // this is used when the page is not empty, and when it is loading more
    @Bind(R.id.main_fab)
    FloatingActionButton fab;

    boolean loadMoreLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* setup burger menu */
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open_string, R.string.drawer_close_string);
        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        navListView.setAdapter(new NavAdapter(activity));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int randomSectionPosition = Util.randInt(0, ImgurConstant.sectionList.size() - 1);
                String section = ImgurConstant.sectionList.get(randomSectionPosition);
                EventBus.getDefault().post(new MainActivity.NewSectionSelectedEvent(section));
            }
        });
        currentSection = ImgurConstant.sectionList.get(0);
        loadMore(currentSection); // default to choose the first one
    }

    /**
     * This method will clear off {@link MainActivity#imgurPaginator}, {@link MainActivity#imageAdapter}
     */
    private void reset() {
        imgurPaginator = null;
        imageAdapter = null;
        imageListView.setAdapter(null);
    }

    private void loadMore(String section) {
        if (imgurPaginator == null) {
            imgurPaginator = new ImgurPaginator(section);
            progressBarMiddle.setVisibility(View.VISIBLE);
            progressBarBottom.setVisibility(View.GONE); // don't show for the 1st time
        } else {
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
        reset();
        loadMore(currentSection);
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
