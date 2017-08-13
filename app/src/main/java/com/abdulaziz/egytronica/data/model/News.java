package com.abdulaziz.egytronica.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdulaziz on 4/26/17.
 */

public class News {

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;

    public News(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
