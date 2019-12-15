/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment.database;


import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.List;

@Entity()
public class CompleteRoute {
    @Embedded
    public Route route;
    @Relation(
            parentColumn = "routeId",
            entityColumn = "route_id"
    )
    public List<Node> nodes;

    @Relation(
            parentColumn = "routeId",
            entityColumn = "route_id"
    )
    public List<Edge> edges;

}
