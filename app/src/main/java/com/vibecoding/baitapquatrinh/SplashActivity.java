package com.vibecoding. baitapquatrinh; //22110334 - Nguyễn Tuấn Huy

import android.content.Intent;
import android.os.Bundle;
import android. os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.vibecoding.baitapquatrinh.Service.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DELAY = 2000; // 2 seconds
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

        // Delay để hiển thị splash screen
        new Handler().postDelayed(() -> {
            checkLoginStatus();
        }, SPLASH_DELAY);
    }

    private void checkLoginStatus() {
        if (sessionManager.isLoggedIn() && sessionManager.isTokenValid()) {
            // User đã đăng nhập, chuyển đến MainActivity
            Log.d(TAG, "User already logged in, redirecting to MainActivity");
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // User chưa đăng nhập, chuyển đến LoginActivity
            Log.d(TAG, "User not logged in, redirecting to LoginActivity");
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}