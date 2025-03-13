package com.denprog.codefestapp.destinations.employee;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.EmployeeActivityViewModel;
import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.JobPosting;
import com.denprog.codefestapp.util.UIState;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EmployeeHomeViewModel extends ViewModel {
    MutableLiveData<List<JobPosting>> listMutableLiveData = new MediatorLiveData<>(Collections.emptyList());
    MutableLiveData<UIState<SearchQueryAndList>> searchUiStateMutableLiveData = new MediatorLiveData<>(null);
    public MutableLiveData<UIState<EmployeeActivityViewModel.EmployeeId>> employeStateMutableLiveData = new MediatorLiveData<>(null);    AppDao appDao;
    @Inject
    public EmployeeHomeViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void getAllJobPosting() {
        CompletableFuture.supplyAsync(new Supplier<List<JobPosting>>() {
            @Override
            public List<JobPosting> get() {
                return appDao.getAllJobPosting();
            }
        }).thenAcceptAsync(new Consumer<>() {
            @Override
            public void accept(List<JobPosting> jobPostingList) {
                searchUiStateMutableLiveData.postValue(new UIState.Success<>(new SearchQueryAndList(jobPostingList, null)));
            }
        });
    }


    public static final class EmployeeCredentials {
        public int employeeId;
        public int userId;

        public EmployeeCredentials(int employeeId, int userId) {
            this.employeeId = employeeId;
            this.userId = userId;
        }
    }

    public static final class SearchQueryAndList {
        public List<JobPosting> jobPostingList;
        @Nullable
        public String searchQuery;

        public SearchQueryAndList(List<JobPosting> jobPostingList, String searchQuery) {
            this.jobPostingList = jobPostingList;
            this.searchQuery = searchQuery;
        }
    }

}
