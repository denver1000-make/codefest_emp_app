package com.denprog.codefestapp.destinations.admin.view_user;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.Credentials;
import com.denprog.codefestapp.room.entity.ReviewStatus;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.util.UIState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ViewUserViewModel extends ViewModel {
    public MutableLiveData<UIState<User>> userToViewMutableLiveData = new MutableLiveData<>(null);
    public MutableLiveData<UIState<String>> downloadInfoMutableLiveData = new MutableLiveData<>(null);
    private AppDao appDao;
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    @Inject
    public ViewUserViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void loadUser(int userId){
        CompletableFuture.supplyAsync(() -> {
            List<User> userList = appDao.getUserById(userId);

            return userList;
        }).thenAcceptAsync(userList -> {
            if (userList.isEmpty()) {
                userToViewMutableLiveData.postValue(new UIState.Fail<>("No User"));
            } else {
                User user = userList.get(0);
                userToViewMutableLiveData.postValue(new UIState.Success<>(user));
            }
        });
    }

    public void downloadUserData(View view, int userId, String name) {
        Context context = view.getContext();
        CompletableFuture.supplyAsync(new Supplier<List<Credentials>>() {
            @Override
            public List<Credentials> get() {
                return appDao.getAllCredentialsByUserIdCredentials(userId);
            }
        }).thenAcceptAsync(credentials -> credentials.forEach(credentials1 -> {
            String[] fileComponents = credentials1.pathToFile.split("/");
            String fileName = fileComponents[fileComponents.length - 1];
            String folderName = fileComponents[fileComponents.length - 2];
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                try {
                    saveToLegacyStorage(name, folderName + File.separator + fileName, context);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                saveFileToScopedStorage(name, folderName + File.separator + fileName, context);
            }
        })).exceptionally(new Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable throwable) {
                Log.e("Error", throwable.getLocalizedMessage());
                return null;
            }
        });
    }

    public void acceptAnApplication (int userId, OnOperationSuccessful onOperationSuccessful) {
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
            appDao.removeUserFromReview(userId);
            appDao.insertReviewStatus(new ReviewStatus(true, userId));
            return null;
        });

        completableFuture.thenAccept(unused -> {
            mHandler.post(onOperationSuccessful::onSuccess);
        });

        completableFuture.exceptionally(throwable -> {
            onOperationSuccessful.onFail(throwable.getLocalizedMessage());
            return null;
        });
    }

    public void rejectAnApplication (int userId, OnOperationSuccessful onOperationSuccessful) {
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync((Supplier<Void>) () -> {
            appDao.removeUserFromReview(userId);
            appDao.insertReviewStatus(new ReviewStatus(false, userId));
            return null;
        });

        completableFuture.thenAccept(new Consumer<Void>() {
            @Override
            public void accept(Void unused) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onOperationSuccessful.onSuccess();
                    }
                });
            }
        });

        completableFuture.exceptionally(throwable -> {
            onOperationSuccessful.onFail(throwable.getLocalizedMessage());
            return null;
        });

    }

    private void saveToLegacyStorage(String folderName, String fileName, Context context) throws IOException {
        File savedFile = new File(context.getFilesDir(), fileName);
        File folderToSaveFile = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), folderName);
        File file = new File(folderToSaveFile, fileName);

        FileInputStream inputStream = new FileInputStream(savedFile);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        FileChannel fisChannel = inputStream.getChannel();
        FileChannel fosChannel = fileOutputStream.getChannel();

        fisChannel.transferTo(0, fisChannel.size(), fosChannel);
        fisChannel.close();
        fosChannel.close();

        /*byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, length);
        }*/

        fileOutputStream.close();
        inputStream.close();
    }

    public void saveFileToScopedStorage(String folderName, String fileName, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        File actualSavedFile = new File(context.getFilesDir(), fileName);
        String[] fileNameSplit = fileName.split("\\.");
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileNameSplit[fileNameSplit.length - 1]);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName.split("/")[1]);
        contentValues.put(MediaStore.Downloads.MIME_TYPE, type);
        contentValues.put(MediaStore.Downloads.RELATIVE_PATH, "Download"+File.separator+folderName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Uri insertedUri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
            if (insertedUri != null) {
                try (OutputStream outputStream = contentResolver.openOutputStream(insertedUri)) {
                    if (outputStream != null) {
                        FileInputStream fileInputStream = new FileInputStream(actualSavedFile);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fileInputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        outputStream.close();
                        fileInputStream.close();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public interface OnOperationSuccessful {
        void onSuccess();
        void onFail(String message);
    }

}