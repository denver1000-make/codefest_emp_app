package com.denprog.codefestapp.destinations.employee.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.JobPostingApplication;
import com.denprog.codefestapp.room.entity.JobPostingApplicationFile;
import com.denprog.codefestapp.util.FileUtil;
import com.denprog.codefestapp.util.OnOperationSuccessful;
import com.denprog.codefestapp.util.UIState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class JobPostingDialogViewModel extends ViewModel {

    MutableLiveData<List<JobPostingApplicationFile>> jobPostingApplicationFiles = new MutableLiveData<List<JobPostingApplicationFile>>(new ArrayList<>());
    public MutableLiveData<UIState<JobPostingApplicationDialogFragment.JobPostingIdAndEmployeeId>> credentialStatus = new MutableLiveData<>(null);
    AppDao appDao;
    private Handler handler = new Handler(Looper.getMainLooper());
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


    public void insertApplication(int employeeId, int jobPostingId, Context context, OnOperationSuccessful<Void> onOperationSuccessful) {
        onOperationSuccessful.onLoading();
        List<JobPostingApplicationFile> jobPostingApplicationFileToInsert = jobPostingApplicationFiles.getValue();
        if (!jobPostingApplicationFileToInsert.isEmpty()) {
            CompletableFuture<Integer> insertApplication = CompletableFuture.supplyAsync(() -> {
                if (appDao.getJobPostingApplicationByEmployeeIdAndJobPostingId(employeeId, jobPostingId).isEmpty()) {
                    return Math.toIntExact(appDao.insertApplication(new JobPostingApplication(jobPostingId, employeeId)));
                } else {
                    throw new RuntimeException("An application is already present for this job.");
                }
            });
            insertApplication.thenAccept(applicationId -> {
                jobPostingApplicationFileToInsert.forEach(jobPostingApplicationFile -> {
                    jobPostingApplicationFile.employeeId = employeeId;
                    jobPostingApplicationFile.jobPostingApplicationId = applicationId;
                    jobPostingApplicationFile.pathOfFile = FileUtil.insertUriToInternalDirectory(jobPostingApplicationFile.pathOfFile, FileUtil.APPLICATION_FILE_LOC, employeeId + "", jobPostingApplicationFile.uri, context);
                    appDao.insertApplicationFile(jobPostingApplicationFile);
                });
                handler.post(() -> onOperationSuccessful.onSuccess(null));
            }).exceptionally(throwable -> {
                onOperationSuccessful.onError(throwable.getLocalizedMessage());
                return null;
            });
            insertApplication.exceptionally(throwable -> {
                handler.post(() -> onOperationSuccessful.onError(throwable.getLocalizedMessage()));
                return 0;
            });
        } else {
            onOperationSuccessful.onError("Empty Application List");
        }
    }
}
