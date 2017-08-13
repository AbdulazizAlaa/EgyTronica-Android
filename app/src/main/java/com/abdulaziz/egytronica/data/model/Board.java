package com.abdulaziz.egytronica.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdulaziz on 3/2/17.
 */

public class Board {

    @SerializedName("id")
    private String id;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("name")
    private String name;
    @SerializedName("code")
    private String code;
    @SerializedName("color_code")
    private int colorCode;
    @SerializedName("status")
    private String status;
    @SerializedName("advice")
    private String advice;
    @SerializedName("output_efficiency")
    private String outputEfficiency;
    @SerializedName("temp")
    private String temp;
    @SerializedName("humidity")
    private String humidity;
    @SerializedName("run_time")
    private String runTime;
    @SerializedName("refresh_time")
    private String refreshTime;
    @SerializedName("last_maintainance")
    private String lastMaintainance;
    @SerializedName("m_type")
    private int memberType;
    @SerializedName("is_owner")
    private boolean isOwner;


    public Board(){
        this("","","","",3,"","","","","","","","", 0, false);
    }

    public Board(String id, String userId, String name, String code, int colorCode, String status, String advice, String outputEfficiency, String temp, String humidity, String runTime, String refreshTime, String lastMaintainance, int memberType, boolean isOwner) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.code = code;
        this.colorCode = colorCode;
        this.status = status;
        this.advice = advice;
        this.outputEfficiency = outputEfficiency;
        this.temp = temp;
        this.humidity = humidity;
        this.runTime = runTime;
        this.refreshTime = refreshTime;
        this.lastMaintainance = lastMaintainance;
        this.memberType = memberType;
        this.isOwner = isOwner;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getOutputEfficiency() {
        return outputEfficiency+"%";
    }

    public void setOutputEfficiency(String outputEfficiency) {
        this.outputEfficiency = outputEfficiency;
    }

    public String getTemp() {
        return temp+" °C";
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity+"%";
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getRefreshTime() {
        return refreshTime+" Min";
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getLastMaintainance() {
        return lastMaintainance;
    }

    public void setLastMaintainance(String lastMaintainance) {
        this.lastMaintainance = lastMaintainance;
    }
}
