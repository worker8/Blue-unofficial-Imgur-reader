package worker8.com.github.jimgur.imgur.gallery_api;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;


public interface ImgurGalleryApi {

    /**
     * Example: https://api.imgur.com/3/gallery/6FAc1
     */
    @GET("/gallery/{id}")
    GalleryResponse getGalleryResponse(@Path("id") String id);

    @GET("/gallery/{id}")
    Observable<GalleryResponse> getGalleryResponseObservable(@Path("id") String id);
}
