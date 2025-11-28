package com.vibecoding.baitapquatrinh.Service;

import com.vibecoding.baitapquatrinh.Model.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    // Cập nhật endpoint mới
    @GET("api/categories")
    Call<List<Category>> getAllCategories();
}
