/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.NodeDAO;
import com.team.macbook.mobileassigment.database.NodeRoomDatabase;
import com.team.macbook.mobileassigment.database.NumberData;

import java.util.Random;


class MyRepository extends ViewModel {
    private final NodeDAO mDBDao;

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

    /**
     * called by the UI to request the generation of a new random number
     */
    public void generateNewNode() {
        Random r = new Random();
        int i1 = r.nextInt(10000 - 1) + 1;
        int i2 = r.nextInt(10000 - 1) + 1;
        new insertAsyncTask(mDBDao).execute(new Node(i1, i2, 0.0, 0.0));
    }

    private static class insertAsyncTask extends AsyncTask<Node, Void, Void> {
        private NodeDAO mAsyncTaskDao;
        private LiveData<NumberData> numberData;

        insertAsyncTask(NodeDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Node... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyRepository", "route id generated: "+params[0].getRoute_id()+"");
            // you may want to uncomment this to check if numbers have been inserted
            //            int ix=mAsyncTaskDao.howManyElements();
            //            Log.i("TAG", ix+"");
            return null;
        }
    }
}
