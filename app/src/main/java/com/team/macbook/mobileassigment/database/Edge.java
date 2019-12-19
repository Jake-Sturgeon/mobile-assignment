/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment.database;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity()
public class Edge {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int edgeId = 0;
    @NonNull
    public String route_id = "";
    @NonNull
    public int order = 0;
    @NonNull
    public double latitude = 0.0;
    @NonNull
    public double longitude = 0.0;

    public Edge(String route_id, int order, double longitude, double latitude) {
        this.route_id = route_id;
        this.order = order;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * getEdgeId
     * @return
     */
    public int getEdgeId() {
        return edgeId;
    }

    /**
     * setEdgeId
     * @param edgeId
     */
    public void setEdgeId(@NonNull int edgeId) {
        this.edgeId = edgeId;
    }

    /**
     * @return
     */
    public int getOrder() {
        return order;
    }

    /**
     * @param order
     */
    public void setOrder(@NonNull int order) {
        this.order = order;
    }

    /**
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     */
    public void setLatitude(@NonNull double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     */
    public void setLongitude(@NonNull double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return
     */
    @NonNull
    public String getroute_id() {
        return route_id;
    }

    /**
     * @param id
     */
    public void setroute_id(@NonNull String id) {
        this.route_id = id;
    }


}
