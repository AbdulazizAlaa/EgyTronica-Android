package com.abdulaziz.egytronica.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by abdulaziz on 10/29/16.
 */

public class Response {

    @SerializedName("message")
    public String status;

    @SerializedName("error")
    public Error error;

    @SerializedName("token")
    public String token;

    @SerializedName("type")
    public int type;

    @SerializedName("user")
    public User user;

    @SerializedName("users")
    public ArrayList<User> users;

    @SerializedName("boards")
    public ArrayList<Board> boards;

    @SerializedName("board")
    public Board board;

    @SerializedName("components")
    public ArrayList<Component> components;

    @SerializedName("component")
    public Component component;

    @SerializedName("news_list")
    public ArrayList<News> newsList;

    @SerializedName("news")
    public News news;

    @SerializedName("events")
    public ArrayList<Event> events;
}


