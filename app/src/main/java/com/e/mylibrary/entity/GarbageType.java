package com.e.mylibrary.entity;

public class GarbageType {

    private int id;
    private String Name;
    private String imgUrl;
    private String introduce;
    private String guide;

    public GarbageType(int id, String name, String imgUrl, String introduce, String guide) {
        this.id = id;
        Name = name;
        this.imgUrl = imgUrl;
        this.introduce = introduce;
        this.guide = guide;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }
}
