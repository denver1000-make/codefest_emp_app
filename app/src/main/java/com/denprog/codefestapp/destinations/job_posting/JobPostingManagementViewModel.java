package com.denprog.codefestapp.destinations.job_posting;

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
public class JobPostingManagementViewModel extends ViewModel {

    MutableLiveData<List<JobPosting>> listMutableLiveData = new MediatorLiveData<>(Collections.emptyList());
    MutableLiveData<EmployeeHomeViewModel.SearchState> searchStateMutableLiveData = new MediatorLiveData<>(null);
    MutableLiveData<RoleState> roleStateMutableLiveData = new MutableLiveData<>();
    AppDao appDao;

    @Inject
    public JobPostingManagementViewModel (AppDatabase appDatabase) {
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

        public static final class OnSearch extends EmployeeHomeViewModel.SearchState {
            EmployeeHomeViewModel.SearchQueryFilterAndList searchQueryFilterAndList;
            public OnSearch(EmployeeHomeViewModel.SearchQueryFilterAndList searchQueryFilterAndList) {
                this.searchQueryFilterAndList = searchQueryFilterAndList;
            }
        }
    }

    public static class RoleState {

        public int userId;

        public RoleState(int userId) {
            this.userId = userId;
        }

        public static final class EmployeeState extends RoleState{
            public int employeeId;

            public EmployeeState(int userId, int employeeId) {
                super(userId);
                this.employeeId = employeeId;
            }
        }

        public static final class EmployerState extends RoleState{
            public int employerId;

            public EmployerState(int userId, int employerId) {
                super(userId);
                this.employerId = employerId;
            }
        }
    }


}
