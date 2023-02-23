package com.e.mylibrary.entity;

public class Comment {

    private int id;
    private int userId;
    private int libraryId;
    private String content;
    private boolean myLikeState;
    private String userName;
    private String createTime;

    public Comment(int id, int userId, int libraryId, String content, boolean myLikeState, String userName, String createTime) {
        this.id = id;
        this.userId = userId;
        this.libraryId = libraryId;
        this.content = content;
        this.myLikeState = myLikeState;
        this.userName = userName;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMyLikeState() {
        return myLikeState;
    }

    public void setMyLikeState(boolean myLikeState) {
        this.myLikeState = myLikeState;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
