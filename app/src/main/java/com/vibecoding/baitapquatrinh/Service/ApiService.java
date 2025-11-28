package com.vibecoding.baitapquatrinh.Service;

import com.vibecoding.baitapquatrinh.Model.LoginRequest;
import com.vibecoding.baitapquatrinh.Model.LoginResponse;
import com.vibecoding.baitapquatrinh.Model.User;
import com.vibecoding.baitapquatrinh.OtpVerificationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("register")
    Call<Void> register(@Body User user);

    @POST("verify-otp")
    Call<Void> verifyOtp(@Body OtpVerificationRequest request);
}
