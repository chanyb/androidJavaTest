package com.example.smartbox_dup.network;

import android.util.Log;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.someString;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static RetrofitManager instance = new RetrofitManager();
    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new myInterceptor()).build();
    private static Retrofit mRetrofit = new Retrofit.Builder().baseUrl(someString.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
            .build();

            private static ruokAPI api = mRetrofit.create(ruokAPI.class);

            public RetrofitManager() {}

            public static RetrofitManager getInstance() {
                return instance;
            }

            public void signUp(JsonObject params) {
                Call<JsonObject> test = api.SignUp(params);
                test.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        Log.i("this", String.valueOf(response.body()));
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("this", String.valueOf(t));
            }
        });
    }

}
