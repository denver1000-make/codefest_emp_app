package com.denprog.codefestapp.destinations.admin.announcement.announcement_viewer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.AnnouncementAttachment;
import com.denprog.codefestapp.util.OnOperationSuccessful;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AnnouncementViewerViewModel extends ViewModel {

    AppDao appDao;

    public MutableLiveData<List<AnnouncementAttachment>> announcementAttachmentMutableLiveData = new MutableLiveData<List<AnnouncementAttachment>>(new ArrayList<>());

    @Inject
    public AnnouncementViewerViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void getAllAttachmentsOfAnnouncement(int announcementId) {
        CompletableFuture.supplyAsync(new Supplier<List<AnnouncementAttachment>>() {
            @Override
            public List<AnnouncementAttachment> get() {
                return appDao.getAllAnnouncementAttachmentByAnnouncementId(announcementId);
            }
        }).thenAcceptAsync(new Consumer<List<AnnouncementAttachment>>() {
            @Override
            public void accept(List<AnnouncementAttachment> announcementAttachmentList) {
                announcementAttachmentMutableLiveData.postValue(announcementAttachmentList);
            }
        });
    }

    public void downloadFileToDownloadsFolder(Context context, String filePath, OnOperationSuccessful<Void> onOperationSuccessful) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, getFileName(filePath));
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, MimeTypeMap.getSingleton().getMimeTypeFromExtension(getFileExtension(filePath)));
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + extractPath(filePath));

        Uri externalUri = MediaStore.Files.getContentUri("external");
        ContentResolver contentResolver = context.getContentResolver();
        Uri fileUri = contentResolver.insert(externalUri, contentValues);

        try (OutputStream fileOutputStream = contentResolver.openOutputStream(fileUri)) {
            File fileToBeExtracted = new File(filePath);
            FileInputStream fis = new FileInputStream(fileToBeExtracted);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }

            fis.close();
            onOperationSuccessful.onSuccess(null);
        } catch (IOException e) {
            onOperationSuccessful.onError(e.getMessage());
        }
    }

    public String getFileName(String filePath) {
        String[] fileSegments = filePath.split("/");
        return fileSegments[fileSegments.length - 1];
    }

    public String getFileExtension(String filePath) {
        String[] fileSegment = filePath.split("\\.");
        return fileSegment[fileSegment.length - 1];
    }

    public String extractPath (String filePath) {
        ArrayList<String> segmentedList = new ArrayList<>(Arrays.asList(filePath.split("/")));
        segmentedList.remove(segmentedList.size() - 1);
        StringBuilder stringBuilder = new StringBuilder();
        segmentedList.forEach(s -> stringBuilder.append(s).append("/"));
        return stringBuilder.toString();
    }
}