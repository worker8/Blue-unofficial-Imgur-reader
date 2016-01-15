package worker8.com.github.jimgur.imgur.paging_api;

import worker8.com.github.jimgur.imgur.ImgurApi;

public class ImgurPaginator {
    public int currentPage;
    public ImgurPaginationApi imgurPaginationApi;
    public String section = "r/aww";
    public String sortImgur = "viral";
    public String sortReddit = "hot";

    public ImgurPaginator() {
        this(0);
    }

    public ImgurPaginator(int currentPage) {
        this.currentPage = currentPage;
    }

    public ImgurPaginator(String section) {
        this.section = section;
    }

    public static ImgurPaginationApi instantiate() {
        return ImgurApi.getBuilder().create(ImgurPaginationApi.class);
    }

    public ImgurPaginationResponse next() {
        if (imgurPaginationApi == null) {
            imgurPaginationApi = ImgurPaginator.instantiate();
        }
        ImgurPaginationResponse tempResponse;
        if (section.contains("r/")) {
            tempResponse = imgurPaginationApi.getImgurPage(section.toLowerCase(), sortReddit.toLowerCase(), currentPage);
        } else {
            tempResponse = imgurPaginationApi.getImgurPage(section.toLowerCase(), sortImgur.toLowerCase(), currentPage);
        }
        currentPage++;
        return tempResponse;
    }

    /**
     * Reset {@link ImgurPaginator#currentPage} to 0
     */
    public void resetPage() {
        currentPage = 0;
    }

}
