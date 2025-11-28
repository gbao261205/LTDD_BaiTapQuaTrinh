package com.vibecoding.baitapquatrinh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tvGreeting = findViewById(R.id.tv_greeting);

        sessionManager = new SessionManager(this);

        imagesList = getListImages();
        ImagesViewPagerAdapter adapter = new ImagesViewPagerAdapter(imagesList);
        viewPager.setAdapter(adapter);

        // Bắt đầu auto slide
        autoSlideImages();

        // Lấy và hiển thị tên người dùng
        displayUserName();
    }

    private void displayUserName() {
        if (sessionManager.isLoggedIn()) {
            User user = sessionManager.getCurrentUser();
            if (user != null) {
                tvGreeting.setText("Hi! " + user.getFull_name());
            }
        }
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
        }, 500, 3000); // Bắt đầu sau 0.5s, lặp lại mỗi 3s
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
