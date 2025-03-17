package com.denprog.codefestapp.destinations.profile;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.util.UIState;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends ViewModel {

    AppDao appDao;

    public MutableLiveData<UIState<User>> mutableUserState = new MutableLiveData<>();
    public MutableLiveData<UIState<Void>> logoutState = new MutableLiveData<>();
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

    public void getUser(int userId) {
        CompletableFuture<User> completableFuture = CompletableFuture.supplyAsync(new Supplier<User>() {
            @Override
            public User get() {
                List<User> user = appDao.getUserById(userId);
                if (user.isEmpty()) {
                    throw new RuntimeException("No Users Found");
                }
                return user.get(0);
            }
        });

        completableFuture.thenAcceptAsync(user -> mutableUserState.postValue(new UIState.Success<>(user)));
        completableFuture.exceptionally(throwable -> {
            mutableUserState.postValue(new UIState.Fail<>(throwable.getLocalizedMessage()));
            return null;
        });
    }

    public void clearSavedLogin() {
        logoutState.setValue(new UIState.Loading<>());
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
            appDao.clearSavedLogins();
            return null;
        });

        completableFuture.thenAccept(unused -> handler.post(() -> logoutState.setValue(new UIState.Success<>(null))));

        completableFuture.exceptionally(throwable -> {
            logoutState.setValue(new UIState.Fail<>(throwable.getLocalizedMessage()));
            return null;
        });
    }

}