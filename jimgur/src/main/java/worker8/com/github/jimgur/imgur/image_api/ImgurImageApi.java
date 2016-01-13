package worker8.com.github.jimgur.imgur.image_api;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;


public interface ImgurImageApi {
    @GET("/image/{id}")
    ImgurImageResponse getImgurImage(@Path("id") String id);

    @GET("/image/{id}")
    Observable<ImgurImageResponse> getImgurImageObservable(@Path("id") String id);
}
