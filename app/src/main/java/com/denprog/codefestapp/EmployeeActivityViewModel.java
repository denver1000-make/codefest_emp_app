package com.denprog.codefestapp;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.util.UIState;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EmployeeActivityViewModel extends ViewModel {

    public MutableLiveData<UIState<EmployeeId>> mutableLiveData = new MediatorLiveData<>(null);
                           
    @Inject
    public EmployeeActivityViewModel() {
    }

    public static final class EmployeeId {
        public int employeeId;
        public int userId;

        public EmployeeId(int employeeId, int userId) {
            this.employeeId = employeeId;
            this.userId = userId;
        }
    }



}
