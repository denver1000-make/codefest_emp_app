package com.denprog.codefestapp.destinations.employee;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.JobPosting;
import com.denprog.codefestapp.util.UIState;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EmployeeHomeViewModel extends ViewModel {

    MutableLiveData<UIState<EmployeeCredentials>> employeeCredentialsUIState = new MutableLiveData<>();

    MutableLiveData<List<JobPosting>> listMutableLiveData = new MediatorLiveData<>(Collections.emptyList());

    AppDao appDao;
    @Inject
    public EmployeeHomeViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void getAllJobPosting() {
        CompletableFuture.supplyAsync(
                () -> appDao.getAllJobPosting()).thenAcceptAsync(
                        jobPostingList -> listMutableLiveData.postValue(jobPostingList));
    }


    public static final class EmployeeCredentials {
        public int employeeId;
        public int userId;

        public EmployeeCredentials(int employeeId, int userId) {
            this.employeeId = employeeId;
            this.userId = userId;
        }
    }

}
