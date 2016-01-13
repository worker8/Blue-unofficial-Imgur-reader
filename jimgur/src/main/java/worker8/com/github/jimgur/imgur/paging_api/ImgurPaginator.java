package worker8.com.github.jimgur.imgur.paging_api;

import worker8.com.github.jimgur.imgur.ImgurApi;

public class ImgurPaginator {
    public int currentPage;
    public ImgurPaginationApi imgurPaginationApi;
    public String section = "hot";
    public String sort = "viral";

    public ImgurPaginator() {
        this(0);
    }

    public ImgurPaginator(int currentPage) {
        this.currentPage = currentPage;
    }

    public static ImgurPaginationApi instantiate() {
        return ImgurApi.getBuilder().create(ImgurPaginationApi.class);
    }

    public ImgurPaginationResponse next() {
        if (imgurPaginationApi == null) {
            imgurPaginationApi = ImgurPaginator.instantiate();
        }
        ImgurPaginationResponse tempResponse = imgurPaginationApi.getImgurPage(section, sort, currentPage);
        currentPage++;
        return tempResponse;
    }

    /**
     * Reset {@link ImgurPaginator#currentPage} to 0
     */
    public void resetPage(){
        currentPage = 0;
    }

}
