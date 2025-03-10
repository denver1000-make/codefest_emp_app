package com.denprog.codefestapp.destinations.employee.dialog;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.JobPostingApplicationFile;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class JobPostingDialogViewModel extends ViewModel {

    MutableLiveData<List<JobPostingApplicationFile>> jobPostingApplicationFiles = new MutableLiveData<List<JobPostingApplicationFile>>(new ArrayList<>());

    AppDao appDao;

    @Inject
    public JobPostingDialogViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void addFile(JobPostingApplicationFile jobPostingApplicationFile) {
        List<JobPostingApplicationFile> jobPostingApplicationFiles1 = jobPostingApplicationFiles.getValue();
        jobPostingApplicationFiles1.add(jobPostingApplicationFile);
        jobPostingApplicationFiles.setValue(jobPostingApplicationFiles1);
    }

    public void remove(int index) {
        List<JobPostingApplicationFile> jobPostingApplicationFiles1 = jobPostingApplicationFiles.getValue();
        jobPostingApplicationFiles1.remove(index);
        jobPostingApplicationFiles.setValue(jobPostingApplicationFiles1);
    }



}
