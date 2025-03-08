package com.denprog.codefestapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EmployeeActivityMainViewModel extends ViewModel {

    public MutableLiveData<Integer> empIdMutableLiveData = new MutableLiveData<>(-1);
    private AppDao appDao;

    @Inject
    public EmployeeActivityMainViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }
}
