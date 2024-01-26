package com.example.userlist.services;

import com.example.userlist.models.PaginatedResource;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UnknownService<T> {
    @GET("/api/unknown")
    Call<PaginatedResource<T>> doGetListResources();
}
