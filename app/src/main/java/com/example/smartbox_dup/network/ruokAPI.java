package com.example.smartbox_dup.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ruokAPI {

    @POST("/users")
    Call<JsonObject> SignUp(
            @Body JsonObject jsonObject
    );


    @GET("/login")
    Call<JsonObject> SignIn(
            @Query(value="username", encoded = true) String username,
            @Query(value="password", encoded = true) String password
    );

    @GET("/user/signup")
    Call<JsonObject> test();

    @GET("/classes/menu")
    Call<JsonObject> getMenuItems(
            @Header(value="X-Parse-Session-Token") String sessionToken
    );
}
