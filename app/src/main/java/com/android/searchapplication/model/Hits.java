package com.android.searchapplication.model;

import com.google.gson.annotations.SerializedName;

public class Hits {
    private int viewType;

    private String title;

    private String url;

    @SerializedName("created_at")
    private String createdAt;

    private String author;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUrl() {
        return url;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }
}
