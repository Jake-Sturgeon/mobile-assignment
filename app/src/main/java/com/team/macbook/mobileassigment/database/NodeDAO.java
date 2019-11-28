/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface NodeDAO {
    @Insert
    void insertAll(Node... node);

    @Insert
    void insert(Node node);

    @Delete
    void delete(Node node);

    // it selects a random element
    @Query("SELECT * FROM node ORDER BY RANDOM() LIMIT 1")
    LiveData<Node> retrieveOneNode();

    @Delete
    void deleteAll(Node... node);

    @Query("SELECT COUNT(*) FROM node")
    int howManyElements();
}
