package com.denprog.codefestapp.destinations.employer.addJobPositng;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.JobPosting;
import com.denprog.codefestapp.util.UIState;

import java.util.concurrent.CompletableFuture;
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



        CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return Math.toIntExact(appDao.insertJobPosting(new JobPosting(postingNameValue, postingDescriptionValue, postingCategoryValue, postingMinSalaryValue, postingMaxSalaryValue, employerId)));
            }
        });
    }

    // TODO: Implement the ViewModel
}