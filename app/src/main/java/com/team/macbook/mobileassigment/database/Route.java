/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment.database;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Entity()
public class Route {
    @PrimaryKey
    @androidx.annotation.NonNull
    private String routeId;
    @androidx.annotation.NonNull
    private String title;

    @NonNull
    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(@NonNull long startDate) {
        this.startDate = startDate;
    }

    @androidx.annotation.NonNull
    private long startDate;

    public Route(String routeId, String title, long startDate) {
        this.routeId = routeId;
        this.startDate = startDate;
        this.title = title;
    }

    @androidx.annotation.NonNull
    public String getRouteId() {
        return routeId;
    }
    public void setRouteId(@androidx.annotation.NonNull String id) {
        this.routeId = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(@androidx.annotation.NonNull String title) {
        this.title = title;
    }


}
