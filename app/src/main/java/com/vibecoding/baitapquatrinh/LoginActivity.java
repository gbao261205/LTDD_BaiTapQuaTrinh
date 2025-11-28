package com.vibecoding.baitapquatrinh;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vibecoding.baitapquatrinh.Model.LoginRequest;
import com.vibecoding.baitapquatrinh.Model.LoginResponse;
import com.vibecoding.baitapquatrinh.Service.ApiClient;
import com.vibecoding.baitapquatrinh.Service.ApiService;
import com.vibecoding.baitapquatrinh.Service.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private TextInputEditText editTextUsername, editTextPassword;
    private MaterialButton buttonLogin;
    private ProgressBar progressBar;
    private TextView textViewRegister;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        initViews();
        setupClickListeners();

        // Use the standardized ApiClient and ApiService
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    private void initViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);
        textViewRegister = findViewById(R.id.textViewRegister);
    }

    private void setupClickListeners() {
        buttonLogin.setOnClickListener(v -> {
            if (validateInput()) {
                performLogin();
            }
        });

        textViewRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private boolean validateInput() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Vui lòng nhập tài khoản");
            editTextUsername.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Vui lòng nhập mật khẩu");
            editTextPassword.requestFocus();
            return false;
        }

        if (password.length() < 3) {
            editTextPassword.setError("Mật khẩu phải có ít nhất 3 ký tự");
            editTextPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void performLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        Log.d(TAG, "Attempting login with username: " + username);

        showLoading(true);

        LoginRequest loginRequest = new LoginRequest(username, password);

        // Call API using Retrofit
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showLoading(false);

                Log.d(TAG, "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Log.d(TAG, "Login response success: " + loginResponse.isSuccess());

                    if (loginResponse.isSuccess()) {
                        // Save session
                        sessionManager.createSession(
                                loginResponse.getToken(),
                                loginResponse.getUser_info()
                        );

                        Toast.makeText(LoginActivity.this,
                                "Đăng nhập thành công! ",
                                Toast.LENGTH_SHORT).show();

                        // Navigate to MainActivity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        String message = loginResponse.getMessage();
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Response error: " + response.code());
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading response: " + e.getMessage());
                    }
                    Toast.makeText(LoginActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "API Call Failed: " + t.getMessage());
                t.printStackTrace();
                Toast.makeText(LoginActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        buttonLogin.setEnabled(!show);
        editTextUsername.setEnabled(!show);
        editTextPassword.setEnabled(!show);
    }
}
