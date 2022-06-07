package com.example.smartbox_dup.network;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.someString;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static RetrofitManager instance = new RetrofitManager();
    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new myInterceptor()).build();
    private static Retrofit mRetrofit = new Retrofit.Builder().baseUrl(someString.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

    ruokAPI api = mRetrofit.create(ruokAPI.class);

    public RetrofitManager() {}

    public static RetrofitManager getInstance() {
        return instance;
    }

    public ruokAPI getAPI() {
        return api;
    }

}
