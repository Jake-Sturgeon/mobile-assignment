/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Edge;
import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.NodeDAO;
import com.team.macbook.mobileassigment.database.NodeRoomDatabase;
import com.team.macbook.mobileassigment.database.Route;
import com.team.macbook.mobileassigment.database.RouteWithNodes;
import java.util.Calendar;

import java.util.List;


class MyRepository extends ViewModel {
    private final NodeDAO mDBDao;
    protected LiveData<Route> currentRoute;


    public MyRepository(Application application) {
        NodeRoomDatabase db = NodeRoomDatabase.getDatabase(application);
        mDBDao = db.myDao();
    }

    /**
     * it gets the data when changed in the db and returns it to the ViewModel
     * @return
     */
    public LiveData<Node> getNode() {
        return mDBDao.retrieveOneNode();
    }

    public LiveData<Route> getRouteFromId(String id) {return mDBDao.getRouteFromId(id);}


    /**
     * called by the UI to request the generation of a new random number
     */
    public void generateNewNode(String id, double lat, double longi, String pic, float temp, float bar) {
        new insertNode(mDBDao).execute(new Node(id, pic, lat, longi, temp, bar));
    }

    public void generateNewEdge(String id, double lat, double longi) {
        Log.d("Edge-Create", id);
        new insertEdge(mDBDao).execute(new Edge(id, 0, lat, longi));
    }

    public void generateNewRoute(String id, String title) {
        new insertRoute(mDBDao).execute(new Route(id, title, Calendar.getInstance().getTimeInMillis()));
    }

    public LiveData<List<RouteWithNodes>> getRoutesWithNodes(){ return mDBDao.getRoutesWithNodes();}

    public LiveData<RouteWithNodes> getRouteWithNodesFromId(String id){
        return mDBDao.getRouteNodesFromId(id);
    }

    public LiveData<CompleteRoute> getCompleteRouteFromId(String id){ return mDBDao.getCompleteRouteFromId(id); }

    public LiveData<List<CompleteRoute>> getAllCompleteRoutes(){ return mDBDao.getAllCompleteRoutes(); }


    private static class insertNode extends AsyncTask<Node, Void, Long> {
        private NodeDAO mAsyncTaskDao;
        insertNode(NodeDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Long doInBackground(final Node... params) {


            return mAsyncTaskDao.insert(params[0]);
        }

        @Override
        protected void onPostExecute(Long id){
            Log.i("MyRepository", "Node id generated: "+id+"");
        }
    }

    private static class insertEdge extends AsyncTask<Edge, Void, Long> {
        private NodeDAO mAsyncTaskDao;
        insertEdge(NodeDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Long doInBackground(final Edge... params) {


            return mAsyncTaskDao.insert(params[0]);

        }

        @Override
        protected void onPostExecute(Long id){
            Log.i("MyRepository", "Node id generated: "+id+"");
        }

    }

    private static class insertRoute extends AsyncTask<Route, Void, Void> {
        private NodeDAO mAsyncTaskDao;
        insertRoute(NodeDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Route... params) {
            mAsyncTaskDao.insertRoute(params[0]);
            return null;
        }


    }

//    private static class getRoute extends AsyncTask<String, Void, Route> {
//        private NodeDAO mAsyncTaskDao;
//        getRoute(NodeDAO dao) {
//            mAsyncTaskDao = dao;
//        }
//        @Override
//        protected Route doInBackground(final String... params) {
//            Route returned = mAsyncTaskDao.getRouteFromId(params[0]);
//            return returned;
//        }
//        protected void onPostExecute(Route result) {
//            showDialog("Downloaded " + result + " bytes");
//            currentRoute = LiveData
//        }
//    }
//
//    private static class insertRoute extends AsyncTask<Route, Void, Void> {
//        private NodeDAO mAsyncTaskDao;
//        insertRoute(NodeDAO dao) {
//            mAsyncTaskDao = dao;
//        }
//        @Override
//        protected Void doInBackground(final Route... params) {
//            mAsyncTaskDao.insertRoute(params[0]);
//            Log.i("MyRepository", "route id generated: "+params[0].getRouteId()+"");
//            return null;
//        }
//    }
}
