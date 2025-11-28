package com.vibecoding.baitapquatrinh.Service; //Hồ Lê Tín Nghĩa - 23162065

import com.vibecoding.baitapquatrinh.Model.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("filter")
    Call<List<Product>> getProductsByCategory(
            @Query("category_id") int categoryId,
            @Query("page") int page,
            @Query("limit") int limit
    );
}