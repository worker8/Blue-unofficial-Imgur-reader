package worker8.com.github.imgurdiscovery.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

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

    /* UI variables */
    @Bind(R.id.main_lv_image_list)
    ListView imageListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    public void loadMore() {
        if (imgurPaginator == null) {
            imgurPaginator = new ImgurPaginator();
        }
        _subscriptions.add(Observable.just(1)
                .map(i -> imgurPaginator.next())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImgurPaginationResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ImgurPaginationResponse imgurPaginationResponse) {
                        Log.d(TAG, "onNext: ");
                        ImageAdapter imageAdapter = new ImageAdapter(activity, imgurPaginationResponse.getImgurDataList());
                        imageListView.setAdapter(imageAdapter);
                    }
                }));
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
