package com.example.smartbox_dup.network;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class myInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request customRequest = chain.request()
                .newBuilder()
                .addHeader("x-parse-application-id","XowknZ3K82ITHYcct8lAgX1e3Gp0hIllEZE4vEwy")
                .addHeader("x-parse-rest-api-key","dYYjbakMFCd6gAeCmPf1cKE25fQz4qxfTR30Qidg")
                .addHeader("x-parse-revocable-session","1")
                .build();

        Response newResponse = chain.proceed(customRequest);
        return newResponse;

    }
}
