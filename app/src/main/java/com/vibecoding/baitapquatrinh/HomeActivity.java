package com.vibecoding.baitapquatrinh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vibecoding.baitapquatrinh.Adapter.CategoryAdapter;
import com.vibecoding.baitapquatrinh.Model.Category;
import com.vibecoding.baitapquatrinh.Service.ApiService;
import com.vibecoding.baitapquatrinh.Service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements CategoryAdapter.OnItemClickListener {

    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupRecyclerView(); // Thiết lập RecyclerView với danh sách rỗng trước
        fetchCategoriesFromApi(); // Gọi API để lấy dữ liệu thật
    }

    private void initViews() {
        categoriesRecyclerView = findViewById(R.id.rv_categories);
    }

    private void setupRecyclerView() {
        // Khởi tạo danh sách rỗng ban đầu
        categoryList = new ArrayList<>();

        // Thiết lập LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoriesRecyclerView.setLayoutManager(layoutManager);

        // Tạo và gán Adapter với danh sách rỗng
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryAdapter.setOnItemClickListener(this);
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    private void fetchCategoriesFromApi() {
        // Lấy service từ RetrofitClient
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Thực hiện cuộc gọi API
        Call<List<Category>> call = apiService.getAllCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Nếu thành công, cập nhật danh sách và thông báo cho Adapter
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    // Xử lý khi có lỗi từ server (ví dụ: 404, 500)
                    Toast.makeText(HomeActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // Xử lý khi có lỗi mạng (ví dụ: không có internet)
                Log.e("API_ERROR", "Lỗi khi gọi API", t);
                Toast.makeText(HomeActivity.this, "Lỗi mạng, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(Category category) {
        Intent intent = new Intent(HomeActivity.this, ProductListActivity.class);
        intent.putExtra("CATEGORY_ID", category.getCategoryId());
        startActivity(intent);
    }
}