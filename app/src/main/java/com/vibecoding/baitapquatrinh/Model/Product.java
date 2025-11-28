package com.vibecoding.baitapquatrinh.Model; //Hồ Lê Tín Nghĩa - 23162065

public class Product {
    private int product_id;
    private String name;
    private double price;
    private String image_url;
    private String description;

    public Product(int product_id, String name, double price, String image_url, String description) {
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.image_url = image_url;
        this.description = description;
    }

    public int getProduct_id() { return product_id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImage_url() { return image_url; }
    public String getDescription() { return description; }
}