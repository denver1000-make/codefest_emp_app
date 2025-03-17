package com.denprog.codefestapp.destinations.dummy;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.Admin;
import com.denprog.codefestapp.room.entity.Employee;
import com.denprog.codefestapp.room.entity.Employer;
import com.denprog.codefestapp.room.entity.SavedUserCredentials;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.util.OnOperationSuccessful;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DummyViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private Handler handler = new Handler(Looper.getMainLooper());
    private AppDao appDao;

    @Inject
    public DummyViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao();
    }

    public void checkIfThereIsSavedLogin(OnOperationSuccessful<User> onOperationSuccessful) {
        CompletableFuture<User> completableFuture = CompletableFuture.supplyAsync(() -> {
            List<SavedUserCredentials> listOfSavedUsers = appDao.getAllSavedUserCredentials();
            if (listOfSavedUsers.isEmpty()) {
                throw new RuntimeException("No Saved User");
            }
            return appDao.getUserById(listOfSavedUsers.get(0).userId).get(0);
        });

        completableFuture.exceptionally(throwable -> {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onOperationSuccessful.onError(throwable.getLocalizedMessage());
                }
            });
            return null;
        });

        completableFuture.thenAccept(user -> handler.post(() -> onOperationSuccessful.onSuccess(user)));
    }

    public void performRoleBasedRedirect (User user, OnUserRoleLoaded onUserRoleLoaded) {
        CompletableFuture.supplyAsync((Supplier<Void>) () -> {
            List<Admin> admin = appDao.getAdminByUserId(user.userId);
            List<Employee> employees = appDao.getEmployeeByUserId(user.userId);
            List<Employer> employers = appDao.getEmployerByUserId(user.userId);
            handler.post(() -> {
                if (!admin.isEmpty()) {
                    onUserRoleLoaded.admin(user, admin.get(0).adminId);
                } else if (!employees.isEmpty()) {
                    onUserRoleLoaded.employee(user, employees.get(0).employeeId);
                } else if (!employers.isEmpty()) {
                    onUserRoleLoaded.employer(user, employers.get(0).employerId);
                } else {
                    onUserRoleLoaded.noRoleAttached();
                }
            });
            return null;
        });
    }

    public interface OnUserRoleLoaded {
        void employee(User user, int employeeId);
        void employer(User user, int employerId);
        void admin(User user, int adminId);
        void noRoleAttached();
    }
}