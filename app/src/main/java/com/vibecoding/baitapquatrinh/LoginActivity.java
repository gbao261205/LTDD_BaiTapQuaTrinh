package com.vibecoding.baitapquatrinh;

import android.content.Intent;
import android.os.Bundle;
import android. text.TextUtils;
import android.util. Log;
import android.view.View;
import android.widget. ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com. google.android.material.textfield.TextInputEditText;
import com.vibecoding.baitapquatrinh. Model.LoginRequest;
import com.vibecoding.baitapquatrinh. Model.LoginResponse;
import com.vibecoding.baitapquatrinh.Service.ApiService;
import com. vibecoding.baitapquatrinh.Service.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private TextInputEditText editTextUsername, editTextPassword;
    private MaterialButton buttonLogin;
    private ProgressBar progressBar;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. activity_login);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Kiểm tra nếu user đã đăng nhập
        if (sessionManager.isLoggedIn()) {
            redirectToMain();
            return;
        }

        initViews();
        setupClickListeners();

        // Initialize API service
        apiService = new ApiService();
    }

    private void initViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id. editTextPassword);
        buttonLogin = findViewById(R.id. buttonLogin);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id. textViewRegister).setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đăng ký sẽ được cập nhật", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupClickListeners() {
        buttonLogin.setOnClickListener(v -> {
            if (validateInput()) {
                performLogin();
            }
        });
    }

    private boolean validateInput() {
        String username = editTextUsername. getText().toString().trim();
        String password = editTextPassword. getText().toString();

        if (TextUtils.isEmpty(username)) {
            editTextUsername. setError("Vui lòng nhập username");
            editTextUsername.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword. setError("Vui lòng nhập password");
            editTextPassword.requestFocus();
            return false;
        }

        if (password.length() < 3) {
            editTextPassword.setError("Password phải có ít nhất 3 ký tự");
            editTextPassword. requestFocus();
            return false;
        }

        return true;
    }

    private void performLogin() {
        String username = editTextUsername.getText().toString(). trim();
        String password = editTextPassword.getText().toString();

        Log.d(TAG, "Attempting login with username: " + username);

        showLoading(true);

        LoginRequest loginRequest = new LoginRequest(username, password);

        apiService.login(loginRequest, new ApiService.LoginCallback() {
            @Override
            public void onSuccess(LoginResponse response) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Log.d(TAG, "Login response success: " + response.isSuccess());

                    if (response.isSuccess()) {
                        // Lưu session
                        sessionManager.createSession(
                                response.getToken(),
                                response.getUser_info()
                        );

                        Toast.makeText(LoginActivity.this,
                                "Đăng nhập thành công!  Xin chào " +
                                        (response.getUser_info() != null ?  response.getUser_info().getFull_name() : ""),
                                Toast.LENGTH_SHORT).show();

                        redirectToMain();
                    } else {
                        Toast.makeText(LoginActivity. this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Log. e(TAG, "Login error: " + error);
                    Toast.makeText(LoginActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View. VISIBLE : View. GONE);
        buttonLogin.setEnabled(! show);
        editTextUsername.setEnabled(!show);
        editTextPassword.setEnabled(!show);
    }

    private void redirectToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}