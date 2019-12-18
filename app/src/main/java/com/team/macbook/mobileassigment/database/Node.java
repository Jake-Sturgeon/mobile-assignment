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
    private double latitude = 0.0;
    private double longitude = 0.0;

    public Node(String route_id, String picture_id, double latitude, double longitude) {
        this.route_id = route_id;
        this.picture_id = picture_id;
        this.latitude = latitude;
        this.longitude = longitude;
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
