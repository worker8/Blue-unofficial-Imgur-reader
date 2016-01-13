package worker8.com.github.jimgur.imgur;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import worker8.com.github.jimgur.imgur.album_api.ImgurAlbumApi;
import worker8.com.github.jimgur.imgur.gallery_api.ImgurGalleryApi;
import worker8.com.github.jimgur.imgur.image_api.ImgurImageApi;

/**
 * sample usage:
 * <pre>
 * ImgurApi.getImageApi().getImgurImageObservable("RZXKnBX")
     .subscribeOn(Schedulers.newThread())
     .observeOn(AndroidSchedulers.mainThread())
     .subscribe(new Observer<ImgurImageResponse>() {
        @Override
        public void onCompleted() {
        Log.d("ImgurApi", "onCompleted: ");
        }

        @Override
        public void onError(Throwable e) {
        Log.d("ImgurApi", "onError: ");
        e.printStackTrace();
        }

        @Override
        public void onNext(ImgurImageResponse imgurImageResponse) {
        Log.d("ImgurApi", "onNext: "+imgurImageResponse.getData().getDatetime());
        }
        });
    </pre>
 */
public class ImgurApi {
    private static final String CLIENT_ID = "f387ac9c58c3ae9";

    public static ImgurAlbumApi getAlbumApi() {
        return getBuilder().create(ImgurAlbumApi.class);
    }

    public static ImgurGalleryApi getGalleryApi() {
        return getBuilder().create(ImgurGalleryApi.class);
    }

    public static ImgurImageApi getImageApi() {
        return getBuilder().create(ImgurImageApi.class);
    }

    public static RestAdapter getBuilder() {
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint("https://api.imgur.com/3/");
        //.setLogLevel(RestAdapter.LogLevel.FULL);

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization", "Client-ID " + CLIENT_ID);
            }
        });
        return builder.build();
    }
}
