package com.vibecoding.baitapquatrinh.Model;

public class User {
    private int user_id;
    private String username;
    private String full_name;
    private String phone;
    private String address;
    private String role;

    // Constructors
    public User() {}

    public User(int user_id, String username, String full_name, String phone, String address, String role) {
        this.user_id = user_id;
        this.username = username;
        this.full_name = full_name;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public User(String username, String full_name, String phone, String address) {
        this.username = username;
        this.full_name = full_name;
        this.phone = phone;
        this.address = address;
    }

    // Getters and Setters
    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFull_name() { return full_name; }
    public void setFull_name(String full_name) { this.full_name = full_name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
