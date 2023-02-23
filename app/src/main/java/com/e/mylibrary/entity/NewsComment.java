package com.e.mylibrary.entity;

public class NewsComment {

    private int id;
    private int newsId;
    private int userId;
    private String content;
    private String createTime;

    public NewsComment(int id, int newsId, int userId, String content, String createTime) {
        this.id = id;
        this.newsId = newsId;
        this.userId = userId;
        this.content = content;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
