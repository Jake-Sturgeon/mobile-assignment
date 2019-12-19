/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment.database;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Node {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    private int id = 0;
    private String route_id = "";
    private String picture_id = "";
    private String icon_id = "";
    private double latitude = 0.0;
    private double longitude = 0.0;
    private float temp;
    private float bar;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private long time;


    public Node(String route_id, String picture_id, String icon_id, double latitude, double longitude, float temp, float bar, long time) {
        this.route_id = route_id;
        this.icon_id = icon_id;
        this.picture_id = picture_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temp = temp;
        this.bar = bar;
        this.time = time;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getBar() {
        return bar;
    }

    public void setBar(float bar) {
        this.bar = bar;
    }

    @androidx.annotation.NonNull
    public int getId() {
        return id;
    }
    public void setId(@androidx.annotation.NonNull int id) {
        this.id = id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getIcon_id() {
        return icon_id;
    }

    public void setIcon_id(String icon_id) {
        this.icon_id = icon_id;
    }

    public String getPicture_id() {
        return picture_id;
    }

    public void setPicture_id(String picture_id) {
        this.picture_id = picture_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
