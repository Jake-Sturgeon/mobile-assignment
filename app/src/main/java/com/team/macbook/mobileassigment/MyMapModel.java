package com.team.macbook.mobileassigment;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.team.macbook.mobileassigment.database.CompleteRoute;

public class MyMapModel extends AndroidViewModel {
    private final MyRepository mRepository;

    private MutableLiveData<CompleteRoute> cr = new MutableLiveData<>();

    public MyMapModel (Application application) {
        super(application);
        // creation and connection to the Repository
        mRepository = new MyRepository(application);

    }

    public void setCR(String id, LifecycleOwner thread) {
        LiveData<CompleteRoute> temp = mRepository.getCompleteRouteFromId(id);
        temp.observe(thread, new Observer<CompleteRoute>() {
            @Override
            public void onChanged(@Nullable final CompleteRoute newValue) {
                cr.postValue(newValue);
            }
        });

    }

    public void generateNewNode(int currentRoute, double lat, double lon) {
        mRepository.generateNewNode(1, lat, lon);
    }

    public void generateNewEdge(int currentRoute ,double lat, double lon) {
        mRepository.generateNewEdge(1, lat, lon);
    }

    public LiveData<CompleteRoute> getCRID(String id) {
        if (cr == null) {
            cr =  new MutableLiveData<CompleteRoute>();
        }
        return cr;
    }

}