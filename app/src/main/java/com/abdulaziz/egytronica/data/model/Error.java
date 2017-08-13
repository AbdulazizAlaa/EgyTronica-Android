package com.abdulaziz.egytronica.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdulaziz on 2/26/17.
 */

public class Error {

    @SerializedName("message")
    public String message;

//    @SerializedName("errors")
//    public Object errors;

    @SerializedName("status_code")
    public int code;

}
