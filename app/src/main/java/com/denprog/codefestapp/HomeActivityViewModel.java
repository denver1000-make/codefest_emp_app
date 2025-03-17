package com.denprog.codefestapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeActivityViewModel extends ViewModel {
    public static final String USER_ID_BUNDLE_KEY = "user_id";
    public static final String EMPLOYER_ID_BUNDLE_KEY = "employer_id";
    public static final String EMPLOYEE_ID_BUNDLE_KEY = "employee_id";
    public static final String ADMIN_ID_BUNDLE_KEY = "admin_id";
    public MutableLiveData<Integer> userIdMutableLiveData = new MutableLiveData<>(-1);

    AppDao appDao;

    @Inject
    public HomeActivityViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void setUserIdMutableLiveData(int userId) {
        this.userIdMutableLiveData.setValue(userId);
    }

    public CompletableFuture<User> fetchUser(int userId) {
        return CompletableFuture.supplyAsync(() -> {
            List<User> listUsers = appDao.getUserById(userId);
            if (!listUsers.isEmpty()) {
                return listUsers.get(0);
            }
            throw new RuntimeException("User Not Found");
        });
    }

}
