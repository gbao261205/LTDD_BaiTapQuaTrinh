package com.vibecoding.baitapquatrinh.Service;

import com.vibecoding.baitapquatrinh.Model.LoginRequest;
import com.vibecoding.baitapquatrinh.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2. http.POST;

public interface ApiInterface {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}