package com.example.obi1.testapplication;

import android.widget.EditText;

import java.io.Serializable;

public class TravelDealNG implements Serializable { //Serializable is the java's approach to declare intent extras explictly. Used to send deal class
    private String id;
    private String title;
    private String description;
    private String price;

    public TravelDealNG() {}

    public TravelDealNG(String title, String description, String price) { //Constructor
        this.setId(getId());
        this.setTitle(title);
        this.setDescription(description);
        this.setPrice(price);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    //private String imageUrl;
}
