//Nguyễn Gia Bảo - 23162006

package com.vibecoding.baitapquatrinh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    private Button buttonBatDau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Ánh xạ các thành phần giao diện
        buttonBatDau = findViewById(R.id.buttonBatDau);

        //Lắng nghe sự kiện nút bắt đầu
        buttonBatDau.setOnClickListener(v -> {
            // Chuyển đến trang /login nếu chưa đăng nhập hoặc trang /main nếu đã đăng nhập
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}