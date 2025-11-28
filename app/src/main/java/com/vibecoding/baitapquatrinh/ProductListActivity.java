package com.vibecoding.baitapquatrinh; //Hồ Lê Tín Nghĩa - 23162065

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.vibecoding.baitapquatrinh.Adapter.ProductAdapter;
import com.vibecoding.baitapquatrinh.Service.ApiService;
import com.vibecoding.baitapquatrinh.Model.Product;
import com.vibecoding.baitapquatrinh.Service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    private int currentPage = 1;
    private boolean isLoading = false;
    private final int LIMIT = 10;
    private int categoryId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CATEGORY_ID")) {
            categoryId = intent.getIntExtra("CATEGORY_ID", 1);
        }

        // 1. Cấu hình RecyclerView
        recyclerView = findViewById(R.id.recyclerProducts);
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);

        // Layout dạng lưới 2 cột
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // 2. Cấu hình Retrofit
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // 3. Hàm gọi API
        loadProducts(apiService);

        // 4. Bắt sự kiện cuộn trang (Scroll Listener)
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // dy > 0 nghĩa là đang cuộn xuống dưới
                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading) {
                        // Nếu đã cuộn đến gần cuối danh sách
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0) {

                            currentPage++; // Tăng trang lên
                            loadProducts(apiService); // Gọi API tiếp
                        }
                    }
                }
            }
        });
    }

    private void loadProducts(ApiService apiService) {
        isLoading = true;

        // Gọi API: categoryId, page, limit
        apiService.getProductsByCategory(categoryId, currentPage, LIMIT)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Product> newProducts = response.body();
                            if (newProducts.size() > 0) {
                                adapter.addData(newProducts); // Thêm vào danh sách hiện tại
                            } else {
                                // Hết dữ liệu
                                Toast.makeText(ProductListActivity.this, "Đã hết sản phẩm!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        isLoading = false;
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        isLoading = false;
                        Toast.makeText(ProductListActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}