package com.android.searchapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    private int page;

    @SerializedName("nbPages")
    private int numOfPages;

    private int hitsPerPage;

    private List<Hits> hits;

    public int getPage() {
        return page;
    }

    public int getHitsPerPage() {
        return hitsPerPage;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public List<Hits> getHits() {
        return hits;
    }
}
