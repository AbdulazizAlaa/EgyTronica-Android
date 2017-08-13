package com.abdulaziz.egytronica.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdulaziz on 3/8/17.
 */

public class Component {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("color_code")
    private int colorCode;
    @SerializedName("type")
    private int type;
    @SerializedName("status")
    private String status;
    @SerializedName("nodes")
    private String nodes;
    @SerializedName("heat_loss")
    private String heatLoss;
    @SerializedName("effect_on_power")
    private String effectOnPower;
    @SerializedName("close")
    private String isPaused;
    @SerializedName("close_time")
    private String pauseTime;

    public Component(){
        this("", "", 0, 0, "", "", "", "", "", "");
    }

    public Component(String id, String name, int colorCode, int type, String status, String nodes, String heatLoss, String effectOnPower, String isPaused, String pauseTime) {
        this.id = id;
        this.name = name;
        this.colorCode = colorCode;
        this.type = type;
        this.status = status;
        this.nodes = nodes;
        this.heatLoss = heatLoss;
        this.effectOnPower = effectOnPower;
        this.isPaused = isPaused;
        this.pauseTime = pauseTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsPaused() {
        return isPaused;
    }

    public void setIsPaused(String isPaused) {
        this.isPaused = isPaused;
    }

    public String getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(String pauseTime) {
        this.pauseTime = pauseTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getHeatLoss() {
        return heatLoss;
    }

    public void setHeatLoss(String heatLoss) {
        this.heatLoss = heatLoss;
    }

    public String getEffectOnPower() {
        return effectOnPower;
    }

    public void setEffectOnPower(String effectOnPower) {
        this.effectOnPower = effectOnPower;
    }
}
