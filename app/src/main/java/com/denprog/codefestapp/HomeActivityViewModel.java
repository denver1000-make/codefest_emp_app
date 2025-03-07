package com.denprog.codefestapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeActivityViewModel extends ViewModel {
    public static final String USER_ID_BUNDLE_KEY = "user_id";

    public MutableLiveData<Integer> userIdMutableLiveData = new MutableLiveData<>(-1);


    @Inject
    public HomeActivityViewModel() {
    }

    public void setUserIdMutableLiveData(int userId) {
        this.userIdMutableLiveData.setValue(userId);
    }

}
