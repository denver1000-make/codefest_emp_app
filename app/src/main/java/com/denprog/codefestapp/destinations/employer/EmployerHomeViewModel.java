package com.denprog.codefestapp.destinations.employer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.JobPosting;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EmployerHomeViewModel extends ViewModel {

    public MutableLiveData<List<JobPosting>> jobPostingMutableLiveData = new MutableLiveData<List<JobPosting>>();
    public MutableLiveData<Integer> empIdMutableLiveData = new MutableLiveData<>(null);


    AppDao appDao;

    @Inject
    public EmployerHomeViewModel (AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void getAllJobPosting () {
        CompletableFuture.supplyAsync(new Supplier<List<JobPosting>>() {
            @Override
            public List<JobPosting> get() {
                return appDao.getAllJobPosting();
            }
        }).thenAcceptAsync(new Consumer<List<JobPosting>>() {
            @Override
            public void accept(List<JobPosting> jobPostingList) {
                jobPostingMutableLiveData.postValue(jobPostingList);
            }
        });
    }

}
