package com.vibecoding.baitapquatrinh.Adapter; //Hồ Lê Tín Nghĩa - 23162065

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.vibecoding.baitapquatrinh.Model.Product;
import com.vibecoding.baitapquatrinh.R;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtName.setText(product.getName());

        // Format giá tiền Việt Nam (VD: 50.000 đ)
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.txtPrice.setText(formatter.format(product.getPrice()));

        // Load ảnh bằng Glide
        Glide.with(context)
                .load(product.getImage_url())
                .placeholder(R.drawable.ic_launcher_background) // Hình chờ
                .error(R.drawable.ic_launcher_foreground)       // Hình lỗi
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Hàm thêm dữ liệu mới khi Lazy Load gọi về
    public void addData(List<Product> newProducts) {
        int startPos = productList.size();
        productList.addAll(newProducts);
        notifyItemRangeInserted(startPos, newProducts.size());
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID từ file XML item_product.xml
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }
}