package com.denprog.codefestapp.destinations.employer.addJobPositng;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.JobPosting;
import com.denprog.codefestapp.util.UIState;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddJobPostingViewModel extends ViewModel {

    public MutableLiveData<UIState<Integer>> mutableLiveDataOfInsertedId = new MutableLiveData<>();
    public ObservableField<String> postingName = new ObservableField<>("");
    public ObservableField<String> postingDescription = new ObservableField<>("");
    public ObservableField<String> postingCategory = new ObservableField<>("");
    public ObservableField<Float> postingMaxSalary = new ObservableField<>(0.0f);

    public ObservableField<Float> postingMinSalary = new ObservableField<>(0.0f);

    private AppDao appDao;
    @Inject
    public AddJobPostingViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void addJobPosting(int employerId) {
        String postingNameValue = postingName.get();
        String postingDescriptionValue = postingDescription.get();
        String postingCategoryValue = postingCategory.get();
        float postingMaxSalaryValue = postingMaxSalary.get();
        float postingMinSalaryValue = postingMinSalary.get();
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> Math.toIntExact(appDao.insertJobPosting(new JobPosting(postingNameValue, postingDescriptionValue, postingCategoryValue, postingMinSalaryValue, postingMaxSalaryValue, employerId))));
        completableFuture.thenAcceptAsync(integer -> mutableLiveDataOfInsertedId.postValue(new UIState.Success<>(integer)));
        completableFuture.exceptionally(throwable -> {
            mutableLiveDataOfInsertedId.postValue(new UIState.Fail<>(throwable.getLocalizedMessage()));
            return 0;
        });
    }

}