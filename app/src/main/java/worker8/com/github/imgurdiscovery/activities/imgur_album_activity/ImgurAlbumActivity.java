package worker8.com.github.imgurdiscovery.activities.imgur_album_activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import worker8.com.github.imgurdiscovery.R;
import worker8.com.github.imgurdiscovery.custom_view.HackyViewPager;
import worker8.com.github.imgurdiscovery.util.RxUtils;
import worker8.com.github.jimgur.imgur.ImgurApi;
import worker8.com.github.jimgur.imgur.album_api.AlbumResponse;
import worker8.com.github.jimgur.imgur.gallery_api.GalleryResponse;

public class ImgurAlbumActivity extends AppCompatActivity {

    SectionsPagerAdapter sectionsPagerAdapter;
    Activity activity;
    private static final String TAG = "ImgurAlbumActivity";
    @Bind(R.id.imgur_grid_container)
    LinearLayout imgurGridContainer;
    @Bind(R.id.imgur_album_grid)
    ImageView imgurAlbumGrid;
    @Bind(R.id.pager)
    HackyViewPager imgurAlbumViewPager;
    @Bind(R.id.imgur_album_grid_view)
    GridView imgurAlbumGridView;
    @Bind(R.id.imgur_album_back_arrow)
    ImageView imgurAlbumBackArrow;

    public View.OnClickListener onClickListenerGridIcon;
    public View.OnClickListener onClickListenerBackIcon;

//    public final static String IMGUR_ALBUM_URL = "IMGUR_ALBUM_URL";
    public Object albumData;
    public String id;
    private CompositeSubscription _subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        try {
            id = getIntent().getDataString();
            Log.d(TAG, "onCreate: id = " + id);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Sorry, an error has occurred when trying to open this Imgur Album or Gallery", Toast.LENGTH_LONG).show();
            finish();
        }
        setContentView(R.layout.activity_imgur_album);
        ButterKnife.bind(this);

        // setup click events
        onClickListenerGridIcon = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgurGridContainer.getVisibility() == View.VISIBLE) {
                    imgurGridContainer.setVisibility(View.GONE);
                    imgurAlbumViewPager.setVisibility(View.VISIBLE);
                } else if (imgurGridContainer.getVisibility() == View.GONE) {
                    imgurGridContainer.setVisibility(View.VISIBLE);
                    imgurAlbumViewPager.setVisibility(View.GONE);
                }
            }
        };

        onClickListenerBackIcon = v -> {
            activity.finish();
        };

        imgurAlbumGrid.setOnClickListener(onClickListenerGridIcon);
        imgurAlbumBackArrow.setOnClickListener(onClickListenerBackIcon);

        imgurAlbumGridView.setOnItemClickListener((parent, v, position, id) -> {
            imgurAlbumViewPager.setCurrentItem(position, true);
            if (imgurGridContainer.getVisibility() == View.VISIBLE) {
                imgurGridContainer.setVisibility(View.GONE);
                imgurAlbumViewPager.setVisibility(View.VISIBLE);
            } else if (imgurGridContainer.getVisibility() == View.GONE) {
                imgurGridContainer.setVisibility(View.VISIBLE);
                imgurAlbumViewPager.setVisibility(View.GONE);
            }
        });

        loadData();
    }

    public void loadData() {
        _subscriptions.add(Observable.just(1)
                .map(i -> {
                    return ImgurApi.getAlbumApi().getAlbumResponse(id);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AlbumResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AlbumResponse albumResponse) {
                        Log.d(TAG, "onNext: ");
                        onDataObtained(albumResponse);
                    }
                }));
    }

    public void onDataObtained(Object _albumData) {
        albumData = _albumData;
        imgurAlbumGridView.setAdapter(new ImageGridAdapter(activity, _albumData));
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        imgurAlbumViewPager.setAdapter(sectionsPagerAdapter);
        imgurAlbumViewPager.setPageTransformer(true, new ParallaxPageTransformer());
        sectionsPagerAdapter.notifyDataSetChanged();
        ((ImageGridAdapter) imgurAlbumGridView.getAdapter()).notifyDataSetChanged();
    }

    /* https://medium.com/@BashaChris/the-android-viewpager-has-become-a-fairly-popular-component-among-android-apps-its-simple-6bca403b16d4 */
    public class ParallaxPageTransformer implements ViewPager.PageTransformer {

        public void transformPage(View view, float position) {

            int pageWidth = view.getWidth();
            ImageView commentImageView = (ImageView) view.findViewById(R.id.imgur_album_image);

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(1);

            } else if (position <= 1) { // [-1,1]

                commentImageView.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(1);
            }


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


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return ImgurAlbumFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            if (albumData instanceof AlbumResponse) {
                return ((AlbumResponse) albumData).getData().getImages().length;
            } else {
                return ((GalleryResponse) albumData).getData().getImages().length;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
//                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
//                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
//                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }


}
