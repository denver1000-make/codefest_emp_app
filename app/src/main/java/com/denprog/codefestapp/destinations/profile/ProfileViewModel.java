package com.denprog.codefestapp.destinations.profile;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.util.UIState;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends ViewModel {

    AppDao appDao;

    public MutableLiveData<UIState<User>> mutableUserState = new MutableLiveData<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    @Inject
    public ProfileViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void determineRole (int userId, OnMessageReceived onMessageReceived) {
        CompletableFuture.supplyAsync(() -> {
            if (!appDao.getEmployeeByUserId(userId).isEmpty()) {
                return "Employee";
            } else if (!appDao.getEmployerByUserId(userId).isEmpty()) {
                return "Employer";
            } else if (!appDao.getAdminByUserId(userId).isEmpty()) {
                return "Admin";
            } else {
                return "User invalid";
            }
        }).thenAccept(s -> handler.post(() -> onMessageReceived.onUserRoleDetermined(s)));
    }
    public interface OnMessageReceived {
        void onUserRoleDetermined(String data);
    }

}