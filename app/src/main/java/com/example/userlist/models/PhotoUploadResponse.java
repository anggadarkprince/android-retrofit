package com.example.userlist.models;

import com.google.gson.annotations.SerializedName;

public class PhotoUploadResponse {
    @SerializedName("no_reference")
    public String noReference;
    @SerializedName("upload_status")
    public boolean uploadStatus;
    @SerializedName("photo")
    public String photo;
}
