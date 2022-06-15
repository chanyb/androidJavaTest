package com.example.smartbox_dup.network;

import android.util.Log;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.someString;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    public enum BACK4APP {
        SUCCESS, FAIL
    }
    private static RetrofitManager instance = new RetrofitManager();
    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new myInterceptor()).build();
    private static Retrofit mRetrofit = new Retrofit.Builder().baseUrl(someString.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();

    private static ruokAPI api = mRetrofit.create(ruokAPI.class);

    public RetrofitManager() {}

    public static RetrofitManager getInstance() {
        return instance;
    }

    public JsonObject signUp(JsonObject params) {
        Call<JsonObject> call = api.SignUp(params);
        JsonObject res = new JsonObject();
        try {
            res = call.execute().body();
            if (res == null) {
                res = new JsonObject();
                res.addProperty("back4app", String.valueOf(BACK4APP.FAIL));
            }
            else res.addProperty("back4app", String.valueOf(BACK4APP.SUCCESS));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public void signIn(JsonObject params) {
        Call<JsonObject> call = api.SignIn(params.get("username").getAsString(), params.get("password").getAsString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("this", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("this", "error", t);
            }
        });
    }


}
