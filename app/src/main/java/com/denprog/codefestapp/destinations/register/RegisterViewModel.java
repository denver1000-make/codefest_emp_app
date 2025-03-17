package com.denprog.codefestapp.destinations.register;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.AccountForReview;
import com.denprog.codefestapp.room.entity.Admin;
import com.denprog.codefestapp.room.entity.Credentials;
import com.denprog.codefestapp.room.entity.Employee;
import com.denprog.codefestapp.room.entity.Employer;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.util.FileUtil;
import com.denprog.codefestapp.util.IDUtil;
import com.denprog.codefestapp.util.SelectedFile;
import com.denprog.codefestapp.util.UIState;
import com.denprog.codefestapp.util.Validator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RegisterViewModel extends ViewModel {
    public ObservableField<String> firstNameField = new ObservableField<>("");
    public ObservableField<String> middleNameField = new ObservableField<>("");
    public ObservableField<String> lastNameField = new ObservableField<>("");
    public ObservableField<String> emailField = new ObservableField<>("");
    public ObservableField<String> passwordField = new ObservableField<>("");
    public ObservableField<String> confirmPasswordField = new ObservableField<>("");
    public MutableLiveData<String> roleMutableLiveData = new MutableLiveData<>("");
    public MutableLiveData<UIState<User>> userMutableLiveData = new MutableLiveData<>(null);
    public MutableLiveData<FileActionState> fileActionStateMutableLiveData = new MutableLiveData<>(null);
    private AppDao appDao;

    @Inject
    public RegisterViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void register(View view, List<SelectedFile> selectedFiles) {
        Context context = view.getContext();
        String firstName = firstNameField.get();
        String middleName = middleNameField.get();
        String lastName = lastNameField.get();
        String email = emailField.get();
        String password = confirmPasswordField.get();
        String confirmPassword = confirmPasswordField.get();

        if (Validator.areInputNull(firstName, middleName, lastName, email, password, confirmPassword)) {
            userMutableLiveData.setValue(new UIState.Fail<>("Empty Field"));
            return;
        }

        if (!password.equals(confirmPassword)) {
            userMutableLiveData.setValue(new UIState.Fail<>("Password Mismatch"));
            return;
        }

        if (!Validator.isEmailValid(email)) {
            userMutableLiveData.setValue(new UIState.Fail<>("Email Invalid Format"));
            return;
        }

        User user = new User(firstName, lastName, middleName, password, email);

        CompletableFuture.supplyAsync(() -> {
            if (!appDao.checkIfEmailAlreadyExist(email).isEmpty()) {
                userMutableLiveData.postValue(new UIState.Fail<>("Email already exist"));
                throw new RuntimeException("User Already Exists");
            }

            int userId = (int) appDao.insertUser(user);
            String roleKey = roleMutableLiveData.getValue();
            if (roleKey.equals("Admin")) {
                appDao.insertAdmin(new Admin(userId));
                user.roleName = "Admin";
            } else if (roleKey.equals("Employee")) {
                Employee employee = new Employee(userId);
                int employeeId = (int) appDao.insertEmployee(employee);
                selectedFiles.forEach(selectedFile -> {
                        saveEmployeeCredentials(selectedFile.uri, selectedFile.fileName, userId, context);
                });
                user.roleName = "Employee";
                appDao.insertAccountReview(new AccountForReview(userId));
            } else if (roleKey.equals("Employer")) {
                Employer employer = new Employer(userId);
                int employerId = (int) appDao.insertEmployer(employer);
                selectedFiles.forEach(selectedFile -> {
                    saveEmployeeCredentials(selectedFile.uri, selectedFile.fileName, userId, context);
                });
                user.roleName = "Employer";
                appDao.insertAccountReview(new AccountForReview(userId));
            }
            // Lol
            user.userId = userId;
            appDao.updateUser(user);
            return user;
        }).thenAccept(unused -> {
            userMutableLiveData.postValue(new UIState.Success<>(user));
        }).exceptionally(throwable -> {
            userMutableLiveData.setValue(new UIState.Fail<>(throwable.getLocalizedMessage()));
            return null;
        });
    }

    private void saveEmployeeCredentials(Uri uri, String actualFileName, int empId, Context context) {
        File credentialFolder = new File(context.getFilesDir(), "employee_" + empId);
        credentialFolder.mkdir();
        File credentialFile = new File(credentialFolder, actualFileName);
        try {
            if (!credentialFile.createNewFile()) {
                String extension = FileUtil.getExtension(actualFileName);
                if (extension != null) {
                    String newFileName = IDUtil.generateRandomCharacters(5) + "." + extension;
                    credentialFile = new File(credentialFolder, newFileName);
                }
            }
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream fos = new FileOutputStream(credentialFile);

            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            inputStream.close();
            fos.close();
            appDao.insertCredential(new Credentials(empId, credentialFile.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class FileActionState {
        public static final class AddFile extends FileActionState{
            public SelectedFile data;

            public AddFile(SelectedFile data) {
                this.data = data;
            }
        }
        public static final class RemoveFile extends FileActionState{
            public int data;

            public RemoveFile(int data) {
                this.data = data;
            }
        }
    }
}