package com.example.userlist.services;

import com.example.userlist.models.PaginatedResource;
import com.example.userlist.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @POST("/api/users")
    Call<User> createUser(@Body User user);

    @GET("/api/users?")
    Call<PaginatedResource<User>> getUserList(@Query("page") String page);

    @FormUrlEncoded
    @POST("/api/users?")
    Call<PaginatedResource<User>> createUserWithField(@Field("name") String name, @Field("job") String job);
}
