package com.e.mylibrary.entity;

public class Love {
    private int id;
    private String name; //公益类别
    private String author; //发布人
    private String activityAt; //项目时间
    private int moneyNow; //已筹善款
    private String description; //公益内容
    private int donateCount; //参捐人数

    private String img;

    private int moneyTotal;

    public Love() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getMoneyTotal() {
        return moneyTotal;
    }

    public void setMoneyTotal(int moneyTotal) {
        this.moneyTotal = moneyTotal;
    }

    public Love(String name, String author, String activityAt, int moneyNow, String description, int donateCount) {
        this.name = name;
        this.author = author;
        this.activityAt = activityAt;
        this.moneyNow = moneyNow;
        this.description = description;
        this.donateCount = donateCount;
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

    public void setName(String loveType) {
        this.name = loveType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getActivityAt() {
        return activityAt;
    }

    public void setActivityAt(String activityAt) {
        this.activityAt = activityAt;
    }

    public int getMoneyNow() {
        return moneyNow;
    }

    public void setMoneyNow(int moneyNow) {
        this.moneyNow = moneyNow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDonateCount() {
        return donateCount;
    }

    public void setDonateCount(int donateCount) {
        this.donateCount = donateCount;
    }

    @Override
    public String toString() {
        return "Love{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", activityAt='" + activityAt + '\'' +
                ", moneyNow=" + moneyNow +
                ", description='" + description + '\'' +
                ", donateCount=" + donateCount +
                '}';
    }
}
