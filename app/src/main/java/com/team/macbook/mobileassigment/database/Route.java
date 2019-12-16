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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Entity()
public class Route {
    @PrimaryKey(autoGenerate = true)
    @androidx.annotation.NonNull
    private int routeId = 0;
    @androidx.annotation.NonNull
    private String title;

    public Route(String title) {
        this.title = title;
    }

    @androidx.annotation.NonNull
    public int getRouteId() {
        return routeId;
    }
    public void setRouteId(@androidx.annotation.NonNull int id) {
        this.routeId = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(@androidx.annotation.NonNull String title) {
        this.title = title;
    }


}
