package com.denprog.codefestapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeActivityViewModel extends ViewModel {
    public static final String USER_ID_BUNDLE_KEY = "user_id";
    public static final String EMPLOYER_ID_BUNDLE_KEY = "employer_id";
    public static final String EMPLOYEE_ID_BUNDLE_KEY = "employee_id";
    public static final String ADMIN_ID_BUNDLE_KEY = "admin_id";
    public MutableLiveData<Integer> userIdMutableLiveData = new MutableLiveData<>(-1);
    @Inject
    public HomeActivityViewModel() {
    }

    public void setUserIdMutableLiveData(int userId) {
        this.userIdMutableLiveData.setValue(userId);
    }

}
