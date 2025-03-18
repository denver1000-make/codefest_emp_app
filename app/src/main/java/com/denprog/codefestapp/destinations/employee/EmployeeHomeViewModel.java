package com.denprog.codefestapp.destinations.employee;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.EmployeeActivityViewModel;
import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.JobPosting;
import com.denprog.codefestapp.util.OnOperationSuccessful;
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
    MutableLiveData<SearchState> searchStateMutableLiveData = new MediatorLiveData<>(null);
    AppDao appDao;
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
        }).thenAcceptAsync(new Consumer<List<JobPosting>>() {
            @Override
            public void accept(List<JobPosting> jobPostingList) {
                listMutableLiveData.postValue(jobPostingList);
            }
        });
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

    public static class SearchState {

        public static final class OnSearch extends SearchState {
            SearchQueryFilterAndList searchQueryFilterAndList;
            public OnSearch(SearchQueryFilterAndList searchQueryFilterAndList) {
                this.searchQueryFilterAndList = searchQueryFilterAndList;
            }
        }
    }

    public void filterForCategory (int minSalary, int maxSalary, String category, String searchQ) {
        CompletableFuture.supplyAsync(new Supplier<List<JobPosting>>() {
            @Override
            public List<JobPosting> get() {
                return appDao.filterByAllPosting(minSalary, maxSalary, category, searchQ);
            }
        }).thenAcceptAsync(jobPostingList -> {
            listMutableLiveData.postValue(jobPostingList);
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
