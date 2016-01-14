package worker8.com.github.imgurdiscovery.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import worker8.com.github.imgurdiscovery.R;
import worker8.com.github.imgurdiscovery.util.RxUtils;
import worker8.com.github.jimgur.imgur.paging_api.ImgurPaginationResponse;
import worker8.com.github.jimgur.imgur.paging_api.ImgurPaginator;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = "MainActivity";
    MainActivity activity;

    /* data models */
    private CompositeSubscription _subscriptions = new CompositeSubscription();
    private ImgurPaginator imgurPaginator;
    ImageAdapter imageAdapter;

    /* UI variables */
    @Bind(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_lv)
    ListView navListView;
    @Bind(R.id.main_lv_image_list)
    ListView imageListView;
    @Bind(R.id.main_pb_loading)
    ProgressBar progressBarMiddle; // this is used when the page is empty
    @Bind(R.id.main_pb_bottom_load_more)
    ProgressBar progressBarBottom; // this is used when the page is not empty, and when it is loading more

    boolean loadMoreLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* setup burger menu */
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.drawer_open_string, R.string.drawer_close_string);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadMore();
    }

    /**
     * This method will clear off {@link MainActivity#imgurPaginator}, {@link MainActivity#imageAdapter}
     */
    private void reset() {
        imgurPaginator = null;
        imageAdapter = null;
        imageListView.setAdapter(null);
    }

    private void loadMore() {
        if (imgurPaginator == null) {
            imgurPaginator = new ImgurPaginator();
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
     * This is called when {@link #loadMore()} obtained more data
     * @param imgurPaginationResponse data that is loaded by {@link #loadMore()}
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
                            loadMore();
                        }
                    }
                }
            });
        }
        imageAdapter.addNewData(imgurPaginationResponse.getData());
        loadMoreLock = false;
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
            loadMore();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
