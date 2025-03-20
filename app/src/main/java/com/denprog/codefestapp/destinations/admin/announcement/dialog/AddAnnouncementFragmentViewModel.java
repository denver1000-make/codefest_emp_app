package com.denprog.codefestapp.destinations.admin.announcement.dialog;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.Announcement;
import com.denprog.codefestapp.room.entity.AnnouncementAttachment;
import com.denprog.codefestapp.util.OnOperationSuccessful;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddAnnouncementFragmentViewModel extends ViewModel {

    public MutableLiveData<List<AnnouncementAttachment>> listMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    private static Handler handler = new Handler(Looper.getMainLooper());
    AppDao appDao;

    @Inject
    public AddAnnouncementFragmentViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void addItem(AnnouncementAttachment announcementAttachment) {
        List<AnnouncementAttachment> announcementAttachmentList = listMutableLiveData.getValue();
        announcementAttachmentList.add(announcementAttachment);
        listMutableLiveData.setValue(announcementAttachmentList);
    }


    public void publishAnnouncement(Context context, int adminId, String announcementName, String announcementDescription, List<AnnouncementAttachment> announcementAttachmentList, OnOperationSuccessful<Void> onOperationFinished) {
        CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                Announcement announcement = new Announcement(adminId, announcementName, announcementDescription);
                return (int) appDao.insertAnnouncement(announcement);
            }
        }).thenAcceptAsync(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                announcementAttachmentList.forEach(announcementAttachment -> {
                    announcementAttachment.announcementId = integer;
                    String updatedPath = insertFileIntoInternalStorage(
                            context,
                            AnnouncementAttachment.ANNOUNCEMENT_ATTACHMENT_FOLDER,
                            adminId + "an" + integer,
                            announcementAttachment.fileName,
                            announcementAttachment.uri);
                    if (updatedPath != null) {
                        announcementAttachment.filePath = updatedPath;
                        appDao.insertAnnouncementAttachments(announcementAttachment);
                        handler.post(() -> onOperationFinished.onSuccess(null));
                    } else {
                        Log.d("AnnouncementFileError", announcementAttachment.fileName + " not inserted");
                    }
                });
            }
        }).exceptionally(new Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable throwable) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onOperationFinished.onError(throwable.getLocalizedMessage());
                    }
                });
                return null;
            }
        });
    }

    public String insertFileIntoInternalStorage(Context context, String internalStorageFolder, String personalFolder, String fileName, Uri uri){
        File internalDirectory = new File(context.getFilesDir(), internalStorageFolder);
        internalDirectory.mkdir();
        File personalDirectory = new File(internalDirectory, personalFolder);
        personalDirectory.mkdir();
        File actualFile = new File(personalDirectory, fileName);

        try {
            actualFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(actualFile);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            byte[] buffer = new byte[1024];
            int length;
            while((length = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            inputStream.close();
            return internalStorageFolder + File.separator + personalFolder + File.separator + fileName;
        } catch (Exception e) {
            return null;
        }
    }

}
