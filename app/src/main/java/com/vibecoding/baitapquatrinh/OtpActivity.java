//Nguyễn Gia Bảo - 23162006

package com.vibecoding.baitapquatrinh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vibecoding.baitapquatrinh.Service.ApiClient;
import com.vibecoding.baitapquatrinh.Service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    private EditText etOtp;
    private Button btnVerify;
    private ApiService apiService;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        etOtp = findViewById(R.id.etOtp);
        btnVerify = findViewById(R.id.btnVerify);

        phone = getIntent().getStringExtra("phone");

        // Use the standardized ApiClient and ApiService
        apiService = ApiClient.getClient().create(ApiService.class);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });
    }

    private void verifyOtp() {
        String otp = etOtp.getText().toString().trim();

        if (otp.length() != 6) {
            Toast.makeText(this, "Please enter a 6-digit OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        OtpVerificationRequest request = new OtpVerificationRequest(phone, otp);

        apiService.verifyOtp(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OtpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(OtpActivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(OtpActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
