package com.denprog.codefestapp.destinations.employer.addJobPositng;

import android.os.Handler;
import android.os.Looper;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.JobPosting;
import com.denprog.codefestapp.util.OnOperationSuccessful;
import com.denprog.codefestapp.util.UIState;
import com.denprog.codefestapp.util.Validator;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddJobPostingViewModel extends ViewModel {

    public MutableLiveData<UIState<Integer>> mutableLiveDataOfInsertedId = new MutableLiveData<>();
    public ObservableField<String> postingName = new ObservableField<>("");
    public ObservableField<String> postingDescription = new ObservableField<>("");
    public ObservableField<String> postingCategory = new ObservableField<>("");
    public ObservableField<String> postingMaxSalary = new ObservableField<>("0");
    public ObservableField<String> postingMinSalary = new ObservableField<>("0");
    public MutableLiveData<UIState<JobPosting>> jobPosting = new MutableLiveData<>(null);
    public Handler handler = new Handler(Looper.getMainLooper());
    private AppDao appDao;
    @Inject
    public AddJobPostingViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void addJobPosting(int employerId) {
        String postingNameValue = postingName.get();
        String postingDescriptionValue = postingDescription.get();
        String postingCategoryValue = postingCategory.get();
        String postingMaxSalaryValueStr = postingMaxSalary.get();
        String postingMinSalaryValueStr = postingMinSalary.get();

        if (Validator.areInputNull(postingMaxSalaryValueStr, postingMinSalaryValueStr)) {
            mutableLiveDataOfInsertedId.setValue(new UIState.Fail<>("Empty Fields"));
            return;
        }

        float postingMinSalaryValue = Float.parseFloat(postingMinSalaryValueStr);
        float postingMaxSalaryValue = Float.parseFloat(postingMaxSalaryValueStr);

        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> Math.toIntExact(appDao.insertJobPosting(new JobPosting(postingNameValue, postingDescriptionValue, postingCategoryValue, postingMinSalaryValue, postingMaxSalaryValue, employerId))));
        completableFuture.thenAcceptAsync(integer -> mutableLiveDataOfInsertedId.postValue(new UIState.Success<>(integer)));
        completableFuture.exceptionally(throwable -> {
            mutableLiveDataOfInsertedId.postValue(new UIState.Fail<>(throwable.getLocalizedMessage()));
            return 0;
        });
    }

    public void loadData(int jobPostingId) {
        CompletableFuture<List<JobPosting>> jobPostingCompletableFuture = CompletableFuture.supplyAsync((Supplier<List<JobPosting>>) () -> appDao.getJobPostingId(jobPostingId));
        jobPostingCompletableFuture.thenAcceptAsync(new Consumer<List<JobPosting>>() {
            @Override
            public void accept(List<JobPosting> jobPostingList) {
                if (!jobPostingList.isEmpty()) {
                    jobPosting.postValue(new UIState.Success<>(jobPostingList.get(0)));
                }
            }
        });
    }

    public void updateJobPosting (OnOperationSuccessful<Integer> onOperationSuccessful) {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                if (jobPosting.getValue() instanceof UIState.Success) {
                    JobPosting jobPostingObj = ((UIState.Success<JobPosting>) jobPosting.getValue()).data;
                    jobPostingObj.postingName = postingName.get();
                    jobPostingObj.postingDescription = postingDescription.get();
                    jobPostingObj.postingCategory = postingCategory.get();

                    String postingMaxSalaryValueStr = postingMaxSalary.get();
                    String postingMinSalaryValueStr = postingMinSalary.get();

                    if (Validator.areInputNull(postingMaxSalaryValueStr, postingMinSalaryValueStr)) {
                        throw new RuntimeException("Empty Input Fields");
                    }

                    jobPostingObj.minSalary = Float.parseFloat(postingMinSalaryValueStr);
                    jobPostingObj.maxSalary = Float.parseFloat(postingMaxSalaryValueStr);

                    appDao.updateJobPosting(jobPostingObj);
                    return jobPostingObj.postingId;
                } else {
                    return -1;
                }
            }
        });
        completableFuture.thenAccept(integer -> {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (integer != -1) {
                        onOperationSuccessful.onSuccess(integer);
                    } else {
                        onOperationSuccessful.onError("Job Posting Does not exist");
                    }
                }
            });
        });

        completableFuture.exceptionally(throwable -> {
            onOperationSuccessful.onError(throwable.getLocalizedMessage());
            return 0;
        });
    }

}