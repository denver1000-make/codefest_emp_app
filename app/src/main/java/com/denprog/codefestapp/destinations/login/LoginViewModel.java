package com.denprog.codefestapp.destinations.login;

import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.AccountForReview;
import com.denprog.codefestapp.room.entity.Admin;
import com.denprog.codefestapp.room.entity.Employee;
import com.denprog.codefestapp.room.entity.Employer;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.util.UIState;
import com.denprog.codefestapp.util.Validator;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    public ObservableField<String> emailField = new ObservableField<>("");
    public ObservableField<String> passwordField = new ObservableField<>("");
    public MutableLiveData<RoleState> loginResultState = new MutableLiveData<>();

    private AppDao appDao;

    @Inject
    public LoginViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao() ;
    }

    public void login(View v) {
        String email = emailField.get();
        String password = passwordField.get();

        if (Validator.areInputNull(email, password)) {
            loginResultState.setValue(new RoleState.Fail("Input Field Empty"));
            return;
        }

        CompletableFuture.supplyAsync(() -> {
            List<User> users = appDao.getUserByEmailAndPassword(email, password);
            if (!users.isEmpty()) {
                return users.get(0);
            } else {
                throw new RuntimeException("User Does Not Exist");
            }
        }).thenAcceptAsync(user -> {
            List<Admin> admin = appDao.getAdminByUserId(user.userId);
            if (!admin.isEmpty()) {
                loginResultState.postValue(new RoleState.AdminState(user));
                return;
            }
            List<Employee> employees = appDao.getEmployeeByUserId(user.userId);
            if (!employees.isEmpty()) {
                loginResultState.postValue(new RoleState.EmployeeState(user));
                return;
            }
            List<Employer> employers = appDao.getEmployerByUserId(user.userId);
            if (!employers.isEmpty()) {
                loginResultState.postValue(new RoleState.EmployerState(user));
                return;
            }
            loginResultState.postValue(new RoleState.Fail("No Role was assigned to the user."));
        }).exceptionally(throwable -> {
            loginResultState.postValue(new RoleState.Fail(throwable.getLocalizedMessage()));
            return null;
        });
    }

    public void checkIfUserIsUnderReview (int userId, OnUserReviewStatus onUserReviewStatus) {
        CompletableFuture<List<AccountForReview>> completableFuture = CompletableFuture.supplyAsync(() -> appDao.getUserReview(userId));

        completableFuture.thenAccept(accountForReviews -> {
            if (accountForReviews.isEmpty()) {
                onUserReviewStatus.onUserIsNotUnderReview();
            } else {
                onUserReviewStatus.onUserIsUnderReview();
            }
        });
    }

    public static class RoleState {

        public static final class AdminState extends RoleState {

            public User user;
            public AdminState(User user) {
                this.user = user;
            }
        }

        public static final class EmployeeState extends RoleState {
            public User user;
            public EmployeeState(User user) {
                this.user = user;
            }
        }

        public static final class EmployerState extends RoleState {
            public User user;
            public EmployerState(User user) {
                this.user = user;
            }
        }

        public static final class Loading extends RoleState {

        }

        public static final class Fail extends RoleState {
            String message;
            public Fail(String message) {
                this.message = message;
            }
        }

    }

    public interface OnUserReviewStatus {
        void onUserIsNotUnderReview();
        void onUserIsUnderReview();
    }


}