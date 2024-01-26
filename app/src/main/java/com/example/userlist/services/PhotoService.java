package com.example.userlist.services;

import com.example.userlist.models.PhotoUploadResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PhotoService {
    @Multipart
    @POST("cargo-manifest/upload-photo")
    Call<PhotoUploadResponse> storePhoto(@Part("no_reference") RequestBody noReference, @Part MultipartBody.Part photo);
}
