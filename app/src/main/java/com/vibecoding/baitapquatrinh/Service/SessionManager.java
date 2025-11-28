package com.vibecoding.baitapquatrinh.Service;

import android.content.Context;
import android. content.SharedPreferences;
import android.util.Log;
import com.vibecoding.baitapquatrinh.Model.User;

public class SessionManager {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_TOKEN = "user_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_ROLE = "role";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref. edit();
    }

    // Lưu thông tin đăng nhập
    public void createSession(String token, User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_TOKEN, token);

        if (user != null) {
            editor. putString(KEY_USER_ID, String.valueOf(user. getUser_id()));
            editor.putString(KEY_USERNAME, user.getUsername());
            editor. putString(KEY_FULL_NAME, user.getFull_name());
            editor.putString(KEY_PHONE, user.getPhone());
            editor.putString(KEY_ADDRESS, user.getAddress());
            editor.putString(KEY_ROLE, user.getRole());
        }

        editor.apply();
        Log.d("SessionManager", "Session created for user: " + (user != null ? user.getUsername() : "Unknown"));
    }

    // Kiểm tra trạng thái đăng nhập
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Lấy thông tin user hiện tại
    public User getCurrentUser() {
        if (! isLoggedIn()) {
            return null;
        }

        User user = new User();
        user.setUser_id(Integer.parseInt(pref.getString(KEY_USER_ID, "0")));
        user.setUsername(pref.getString(KEY_USERNAME, ""));
        user. setFull_name(pref.getString(KEY_FULL_NAME, ""));
        user.setPhone(pref. getString(KEY_PHONE, ""));
        user.setAddress(pref.getString(KEY_ADDRESS, ""));
        user.setRole(pref.getString(KEY_ROLE, ""));

        return user;
    }

    // Lấy token
    public String getToken() {
        return pref.getString(KEY_USER_TOKEN, null);
    }

    // Đăng xuất
    public void logout() {
        editor. clear();
        editor. apply();
        Log.d("SessionManager", "User logged out");
    }

    // Kiểm tra token có hết hạn không (có thể thêm logic kiểm tra JWT)
    public boolean isTokenValid() {
        String token = getToken();
        // TODO: Thêm logic kiểm tra token expiry nếu cần
        return token != null && !token.isEmpty();
    }
}