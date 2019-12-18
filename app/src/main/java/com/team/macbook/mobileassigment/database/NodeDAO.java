/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface NodeDAO {
    @Insert
    void insertAll(Node... node);

    @Insert
    long insert(Node node);

    @Delete
    void delete(Node node);

    // it selects a random element
    @Query("SELECT * FROM node ORDER BY RANDOM() LIMIT 1")
    LiveData<Node> retrieveOneNode();

    @Delete
    void deleteAll(Node... node);

    @Query("SELECT COUNT(*) FROM node")
    int howManyElements();

    @Insert
    void insertAllRoutes(Route... route);

    @Insert
    long insertRoute(Route route);

    @Delete
    void deleteRoute(Route route);

    // it selects a random element
    @Query("SELECT * FROM route ORDER BY RANDOM() LIMIT 1")
    Route retrieveOneRoute();

    @Delete
    void deleteAllRoutes(Route... route);

    @Query("SELECT COUNT(*) FROM route")
    int howManyRouteElements();

    @Transaction
    @Query("SELECT * FROM route")
    LiveData<List<RouteWithNodes>> getRoutesWithNodes();

    @Transaction
    @Query("SELECT * FROM route")
    List<RouteWithEdges> getRoutesWithEdges();

    @Transaction
    @Query("SELECT * FROM route WHERE routeId == :id")
    LiveData<RouteWithNodes> getRouteNodesFromId(String id);

    @Query("SELECT * FROM route WHERE routeId == :id")
    LiveData<Route> getRouteFromId(String id);

    @Transaction
    @Query("SELECT * FROM route WHERE routeId == :id")
    LiveData<CompleteRoute> getCompleteRouteFromId(String id);

    @Transaction
    @Query("SELECT * FROM route")
    LiveData<List<CompleteRoute>> getAllCompleteRoutes();

}
