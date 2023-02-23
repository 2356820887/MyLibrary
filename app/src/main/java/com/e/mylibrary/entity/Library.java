package com.e.mylibrary.entity;

public class Library {

    private int id;
    private String name;
    private String address;
    private String imgUrl;
    private String businessHours;
    private String businessState;
    private String description;

    public Library(int id, String name, String address, String imgUrl, String businessHours, String businessState, String description) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.imgUrl = imgUrl;
        this.businessHours = businessHours;
        this.businessState = businessState;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getBusinessState() {
        return businessState;
    }

    public void setBusinessState(String businessState) {
        this.businessState = businessState;
    }
}
