package com.abdulaziz.egytronica.data.model;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Header;

/**
 * Created by abdulaziz on 1/1/17.
 */

public class Request {

    private static Request request;


    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("password")
    private String password;
    @SerializedName("token")
    private String token;
    @SerializedName("registration_id")
    private String registrationID;
    @SerializedName("pin")
    private String pin;
    @SerializedName("user")
    private User user;
    @SerializedName("board")
    private Board board;
    @SerializedName("boards")
    private Board boards;
    @SerializedName("component")
    private Component component;

    public static Request getInstance(){
        if(request == null){
            request = new Request();
        }

        return request;
    }

    private Request() { this("","","","","","","","", new User(), new Board(), new Board(), new Component()); }

    public Request(String id, String name, String email, String phone, String password, String token, String registrationID, String pin, User user, Board board, Board boards, Component component) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.token = token;
        this.registrationID = registrationID;
        this.pin = pin;
        this.user = user;
        this.board = board;
        this.boards = boards;
        this.component = component;
    }

    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Board getBoards() {
        return boards;
    }

    public void setBoards(Board boards) {
        this.boards = boards;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone.replace(" ","");
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
