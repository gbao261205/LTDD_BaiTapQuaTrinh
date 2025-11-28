package com.vibecoding.baitapquatrinh. Service;

import android.util.Log;
import com.vibecoding.baitapquatrinh.Model.LoginRequest;
import com. vibecoding.baitapquatrinh.Model.LoginResponse;
import com.vibecoding. baitapquatrinh.Model. User;

import org.json.JSONException;
import org. json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent. Executors;

import okhttp3. MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiService {
    private static final String BASE_URL = "https://ninety-papers-call.loca.lt/";
    private static final String TAG = "ApiService";
    private OkHttpClient client;
    private ExecutorService executor;

    public ApiService() {
        client = new OkHttpClient. Builder()
                .connectTimeout(30, java.util.concurrent. TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent. TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent. TimeUnit.SECONDS)
                .build();
        executor = Executors.newFixedThreadPool(4);
    }

    public interface LoginCallback {
        void onSuccess(LoginResponse response);
        void onError(String error);
    }

    public void login(LoginRequest loginRequest, LoginCallback callback) {
        executor.execute(() -> {
            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody. put("username", loginRequest.getUsername());
                jsonBody. put("password", loginRequest.getPassword());

                Log.d(TAG, "Request URL: " + BASE_URL + "login. php");
                Log.d(TAG, "Request Body: " + jsonBody.toString());

                RequestBody body = RequestBody.create(
                        MediaType. parse("application/json"),
                        jsonBody.toString()
                );

                Request request = new Request. Builder()
                        .url(BASE_URL + "login. php")
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        . addHeader("Accept", "application/json")
                        . build();

                Response response = client.newCall(request).execute();

                Log.d(TAG, "Response Code: " + response. code());

                if (response. body() != null) {
                    String responseBody = response.body(). string();
                    Log.d(TAG, "Response Body: " + responseBody);

                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = parseLoginResponse(responseBody);
                        callback. onSuccess(loginResponse);
                    } else {
                        try {
                            JSONObject errorJson = new JSONObject(responseBody);
                            String errorMessage = errorJson. optString("message", "Đăng nhập thất bại");
                            callback. onError(errorMessage);
                        } catch (JSONException e) {
                            callback.onError("Lỗi server: " + response.code());
                        }
                    }
                } else {
                    callback.onError("Không có phản hồi từ server");
                }

            } catch (IOException e) {
                Log.e(TAG, "Network Error: " + e. getMessage());
                callback.onError("Lỗi kết nối: " + e.getMessage());
            } catch (JSONException e) {
                Log.e(TAG, "JSON Error: " + e.getMessage());
                callback.onError("Lỗi dữ liệu: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Unexpected Error: " + e.getMessage());
                callback.onError("Lỗi không xác định: " + e.getMessage());
            }
        });
    }

    private LoginResponse parseLoginResponse(String responseBody) throws JSONException {
        JSONObject jsonObject = new JSONObject(responseBody);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setSuccess(jsonObject.optBoolean("success", false));
        loginResponse.setMessage(jsonObject.optString("message", ""));

        if (jsonObject.has("token")) {
            loginResponse.setToken(jsonObject.getString("token"));
        }

        if (jsonObject.has("user_info")) {
            JSONObject userJson = jsonObject.getJSONObject("user_info");
            User user = new User();
            user.setUser_id(userJson.optInt("user_id", 0));
            user.setUsername(userJson.optString("username", ""));
            user.setFull_name(userJson.optString("full_name", ""));
            user. setPhone(userJson.optString("phone", ""));
            user. setAddress(userJson.optString("address", ""));
            user. setRole(userJson.optString("role", "customer"));

            loginResponse.setUser_info(user);
        }

        return loginResponse;
    }
}