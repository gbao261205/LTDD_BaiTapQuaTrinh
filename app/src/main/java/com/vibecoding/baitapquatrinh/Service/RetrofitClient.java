package com.vibecoding.baitapquatrinh.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://food-api-backend-lerp.onrender.com/api/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            // Tạo HttpLoggingInterceptor để log request/response
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor. setLevel(HttpLoggingInterceptor.Level.BODY);

            // Tạo OkHttpClient
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, java.util.concurrent.TimeUnit. SECONDS)
                    .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(30, java.util. concurrent.TimeUnit.SECONDS)
                    .build();

            // Tạo Gson
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // Tạo Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    public static ApiInterface getApiService() {
        return getRetrofit().create(ApiInterface.class);
    }
}