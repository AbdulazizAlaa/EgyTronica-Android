package com.abdulaziz.egytronica.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdulaziz on 3/4/17.
 */

public class Pivot {


    @SerializedName("type")
    private int type;

    public Pivot(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
