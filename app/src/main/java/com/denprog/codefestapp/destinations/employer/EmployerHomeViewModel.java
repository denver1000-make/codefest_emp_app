package com.denprog.codefestapp.destinations.employer;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.destinations.employee.EmployeeHomeViewModel;
import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.JobPosting;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EmployerHomeViewModel extends ViewModel {

    MutableLiveData<List<JobPosting>> listMutableLiveData = new MediatorLiveData<>(Collections.emptyList());
    MutableLiveData<EmployeeHomeViewModel.SearchState> searchStateMutableLiveData = new MediatorLiveData<>(null);
    public MutableLiveData<Integer> empIdMutableLiveData = new MutableLiveData<>(null);


    AppDao appDao;

    @Inject
    public EmployerHomeViewModel (AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void getAllJobPosting (int employerId) {
        CompletableFuture.supplyAsync(new Supplier<List<JobPosting>>() {
            @Override
            public List<JobPosting> get() {
                return appDao.getAllJobPostingCreatedBySpecificEmployer(employerId);
            }
        }).thenAcceptAsync(new Consumer<List<JobPosting>>() {
            @Override
            public void accept(List<JobPosting> jobPostingList) {
                listMutableLiveData.postValue(jobPostingList);
            }
        });
    }



    public void filterByAll(int minSalary, int maxSalary, String category, String searchQ) {
        CompletableFuture.supplyAsync(new Supplier<List<JobPosting>>() {
            @Override
            public List<JobPosting> get() {
                return appDao.maxFilter(minSalary, maxSalary, category, searchQ);
            }
        }).thenAcceptAsync(jobPostingList -> {
            listMutableLiveData.postValue(jobPostingList);
        });
    }

    public void filterByCategoryAndSearchQ (String category, String searchQ) {
        CompletableFuture.supplyAsync(new Supplier<List<JobPosting>>() {
            @Override
            public List<JobPosting> get() {
                return appDao.categoryAndSearchQFilter(category, searchQ);
            }
        }).thenAcceptAsync(new Consumer<List<JobPosting>>() {
            @Override
            public void accept(List<JobPosting> jobPostingList) {
                listMutableLiveData.postValue(jobPostingList);
            }
        });
    }

    public static final class SearchQueryFilterAndList {
        @Nullable
        public String searchQuery = null;
        public int maxSalary = -1;
        public int minSalary = -1;
        public String category = null;
        public SearchQueryFilterAndList() {
        }
    }

}
