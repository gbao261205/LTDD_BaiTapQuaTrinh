package com.vibecoding. baitapquatrinh.Model;

public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private User user_info;

    public LoginResponse() {}

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this. success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public User getUser_info() { return user_info; }
    public void setUser_info(User user_info) { this.user_info = user_info; }
}