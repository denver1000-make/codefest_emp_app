package com.denprog.codefestapp.destinations.employer.applicantsViewer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.JobPostingApplication;
import com.denprog.codefestapp.room.entity.JobPostingApplicationFile;
import com.denprog.codefestapp.room.entity.PrivateChatThread;
import com.denprog.codefestapp.room.view.JobPostingApplicationAndEmployeeInfo;
import com.denprog.codefestapp.util.FileUtil;
import com.denprog.codefestapp.util.OnOperationSuccessful;
import com.denprog.codefestapp.util.UIState;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class JobApplicantViewerViewModel extends ViewModel {
    private AppDao appDao;
    private Handler handler = new Handler(Looper.getMainLooper());
    public MutableLiveData<UIState<Integer>> mutableEmployerIdLiveData = new MutableLiveData<>(null);
    @Inject
    public JobApplicantViewerViewModel (AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public LiveData<List<JobPostingApplicationAndEmployeeInfo>>  getAllJobPostingApplication(int jobPostingId) {
         return appDao.getAllJobPostingApplicationById(jobPostingId);
    }

    public void downloadApplicationFiles(int jobPostingApplicationId, Context context, FileDownloadProgress fileDownloadProgress) {
        fileDownloadProgress.onStart();
        CompletableFuture<List<JobPostingApplicationFile>> jobPostingApplicationFileCompletableFuture = CompletableFuture.supplyAsync(new Supplier<List<JobPostingApplicationFile>>() {
            @Override
            public List<JobPostingApplicationFile> get() {

                return appDao.getAllFiles(jobPostingApplicationId);
            }
        });

        jobPostingApplicationFileCompletableFuture.thenAccept(jobPostingApplicationFiles -> {
            jobPostingApplicationFiles.forEach(jobPostingApplicationFile -> {
                FileUtil.transFileToPublicDownloadFolderFromInternalStorage(jobPostingApplicationFile.pathOfFile, jobPostingApplicationFile.employeeId, context);
            });
            fileDownloadProgress.onFinish();
        });
        jobPostingApplicationFileCompletableFuture.exceptionally(throwable -> {
            fileDownloadProgress.onFail(throwable.getMessage());
            return Collections.emptyList();
        });
    }

    public void checkIfAThreadExist(int employeeId, int employerId, OnOperationSuccessful<Integer> onOperationSuccessful) {
        CompletableFuture<Integer> chatThreadListCompletableFuture = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                List<PrivateChatThread> thread = appDao.getAllChatThread(employeeId, employerId);
                if (thread.isEmpty()) {
                    return (int) appDao.insertChatThread(new PrivateChatThread(employeeId, employerId));
                } else {
                    return thread.get(0).threadId;
                }
            }
        });

        chatThreadListCompletableFuture.thenAcceptAsync(new Consumer<Integer>() {
            @Override
            public void accept(Integer threadId) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onOperationSuccessful.onSuccess(threadId);
                    }
                });
            }
        });

        chatThreadListCompletableFuture.exceptionally(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) {
                onOperationSuccessful.onError(throwable.getLocalizedMessage());
                return 0;
            }
        });
    }

    public interface FileDownloadProgress {
        void onStart();
        void onLoading();
        void onFinish();
        void onFail(String errorMessage);
    }

}
