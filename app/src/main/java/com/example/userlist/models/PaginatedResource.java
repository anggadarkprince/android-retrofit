package com.example.userlist.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaginatedResource<T> {
    @SerializedName("page")
    public Integer page;
    @SerializedName("per_page")
    public Integer perPage;
    @SerializedName("total")
    public Integer total;
    @SerializedName("total_pages")
    public Integer totalPages;
    @SerializedName("data")
    public List<T> data = null;
}
