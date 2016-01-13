package worker8.com.github.jimgur.imgur.album_api;


import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface ImgurAlbumApi {
    /**
     * Example: https://api.imgur.com/3/album/AcSlI
     */
    @GET("/album/{id}")
    AlbumResponse getAlbumResponse(@Path("id") String id);

    @GET("/album/{id}")
    Observable<AlbumResponse> getAlbumResponseObservable(@Path("id") String id);
}
