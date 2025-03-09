package com.denprog.codefestapp.destinations.admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class AdminHomeViewModel extends ViewModel {
    AppDao appDao;

    MutableLiveData<List<User>> credentialsList = new MutableLiveData<>(Collections.emptyList());
    public MutableLiveData<UserReviewStatus> userReviewStatusMutableLiveData = new MutableLiveData<>();

    @Inject
    public AdminHomeViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void getAccountsToReview() {
        CompletableFuture.supplyAsync(() -> appDao.getAllAccountsForReview()).thenAcceptAsync(users -> credentialsList.postValue(users));
    }

    public static final class UserReviewStatus {
        public int userId;
        public boolean reviewStatus;

        public UserReviewStatus(int userId, boolean reviewStatus) {
            this.userId = userId;
            this.reviewStatus = reviewStatus;
        }
    }

}
