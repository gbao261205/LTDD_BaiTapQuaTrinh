package com.vibecoding.baitapquatrinh.Model; //Nguyễn Trần Nhật Nam - 23162062

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("name")
    private String name;

    @SerializedName("image_url")
    private String imageUrl;

    public int getId() { return categoryId; }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}