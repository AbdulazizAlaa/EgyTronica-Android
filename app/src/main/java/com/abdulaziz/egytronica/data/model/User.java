package com.abdulaziz.egytronica.data.model;

import android.util.Log;

import com.abdulaziz.egytronica.data.local.PreferencesHelper;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.google.gson.annotations.SerializedName;

/**
 * Created by abdulaziz on 10/29/16.
 */

public class User {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("email")
    private String email;
    @SerializedName("type")
    private int type;
    @SerializedName("pivot")
    private Pivot pivot;

    public User(){
        this("","","","",0, null);
    }

    public User(String id, String name, String phone, String email, int type, Pivot pivot) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.type = type;
        this.pivot = pivot;
    }

    public Pivot getPivot() {
        return pivot;
    }

    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
