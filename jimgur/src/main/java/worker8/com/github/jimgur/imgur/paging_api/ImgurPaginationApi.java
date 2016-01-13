package worker8.com.github.jimgur.imgur.paging_api;


import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface ImgurPaginationApi {
    // full url example: https://api.imgur.com/3/gallery/hot/viral/0
    // example base endpoint: https://api.imgur.com/3
    // example enpoint: /gallery/hot/viral/0
    @GET("/gallery/{section}/{sort}/{page}")
    ImgurPaginationResponse getImgurPage(@Path("section") String section, @Path("sort") String sort, @Path("page") int page);

    @GET("/gallery/{section}/{sort}/{page}")
    Observable<ImgurPaginationResponse> getImgurPageObservable(@Path("section") String section, @Path("sort") String sort, @Path("page") int page);
}
