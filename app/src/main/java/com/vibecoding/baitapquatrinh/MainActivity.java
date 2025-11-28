package com.vibecoding.baitapquatrinh; //Phan Văn Tài mssv: 23162086

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vibecoding.baitapquatrinh.Model.User;
import com.vibecoding.baitapquatrinh.Service.SessionManager;
import com.vibecoding.baitapquatrinh.Slider_Images_ViewPager.Images;
import com.vibecoding.baitapquatrinh.Slider_Images_ViewPager.ImagesViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Images> imagesList;
    private Timer timer;
    private SessionManager sessionManager;
    private TextView tvGreeting;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tvGreeting = findViewById(R.id.tv_greeting);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        sessionManager = new SessionManager(this);

        imagesList = getListImages();
        ImagesViewPagerAdapter adapter = new ImagesViewPagerAdapter(imagesList);
        viewPager.setAdapter(adapter);

        autoSlideImages();
        displayUserName();
        setupBottomNavigation();
    }

    private void displayUserName() {
        if (sessionManager.isLoggedIn()) {
            User user = sessionManager.getCurrentUser();
            if (user != null) {
                tvGreeting.setText("Hi! " + user.getFull_name());
            }
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_profile) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.navigation_home) {
                    // Already on home screen
                    return true;
                }
                return false;
            }
        });
    }

    private List<Images> getListImages() {
        List<Images> list = new ArrayList<>();
        list.add(new Images("http://app.iotstar.vn:8081/appfoods/flipper/quangcao.png"));
        list.add(new Images("http://app.iotstar.vn:8081/appfoods/flipper/coffee.jpg"));
        list.add(new Images("http://app.iotstar.vn:8081/appfoods/flipper/companypizza.jpeg"));
        list.add(new Images("http://app.iotstar.vn:8081/appfoods/flipper/flipper.jpg"));
        return list;
    }

    private void autoSlideImages() {
        if (imagesList == null || imagesList.isEmpty() || viewPager == null) {
            return;
        }

        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItems = imagesList.size() - 1;
                        if (currentItem < totalItems) {
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
