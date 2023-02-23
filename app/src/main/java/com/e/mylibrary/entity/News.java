package com.e.mylibrary.entity;

public class News {

    private int id;
    private int type;
    private String title;
    private String imgUrl;
    private String content;
    private String author;
    private String createTime;

    public News(int id, int type, String title, String imgUrl, String content, String author, String createTime) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.imgUrl = imgUrl;
        this.content = content;
        this.author = author;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
