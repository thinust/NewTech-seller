package com.oriensolutions.newtechseller.model;

public class Item {

    private String brand;
    private String model;
    private String color;
    private double price;
    private String condition;
    private String name;
    private int qty;
    private String description;
    private String image;
    private String id;

    public Item() {
    }

    public Item(String brand, String model, String color, double price, String condition, String name, int qty, String description, String image, String id) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.price = price;
        this.condition = condition;
        this.name = name;
        this.qty = qty;
        this.description = description;
        this.image = image;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
