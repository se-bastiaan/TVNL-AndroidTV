package eu.se_bastiaan.tvnl.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RadioboxSearch<T> {

    @SerializedName("page-index")
    private Integer pageIndex;
    @SerializedName("max-results")
    private Integer maxResults;
    @SerializedName("total-pages")
    private Integer totalPages;
    @SerializedName("total-results")
    private Integer totalResults;
    private List<T> results;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public List<T> getResults() {
        return results;
    }

}
