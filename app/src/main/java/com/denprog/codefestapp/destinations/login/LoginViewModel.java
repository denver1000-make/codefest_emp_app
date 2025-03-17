package com.denprog.codefestapp.destinations.login;

import android.os.Handler;
import android.os.Looper;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denprog.codefestapp.destinations.dummy.DummyViewModel;
import com.denprog.codefestapp.room.AppDatabase;
import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.AccountForReview;
import com.denprog.codefestapp.room.entity.Admin;
import com.denprog.codefestapp.room.entity.Employee;
import com.denprog.codefestapp.room.entity.Employer;
import com.denprog.codefestapp.room.entity.SavedUserCredentials;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.util.OnOperationSuccessful;
import com.denprog.codefestapp.util.Validator;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    public ObservableField<String> emailField = new ObservableField<>("");
    public ObservableField<String> passwordField = new ObservableField<>("");
    public MutableLiveData<RoleState> loginResultState = new MutableLiveData<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private AppDao appDao;

    @Inject
    public LoginViewModel(AppDatabase appDatabase) {
        this.appDao = appDatabase.getAppDao() ;
    }

    public void login() {
        String email = emailField.get();
        String password = passwordField.get();

        if (Validator.areInputNull(email, password)) {
            loginResultState.setValue(new RoleState.Fail("Input Field Empty"));
            return;
        }

        CompletableFuture<User> completableFuture = CompletableFuture.supplyAsync(() -> {
            List<User> users = appDao.getUserByEmailAndPassword(email, password);
            if (!users.isEmpty()) {
                return users.get(0);
            } else {
                throw new RuntimeException("User Does Not Exist");
            }
        });

        completableFuture.thenAcceptAsync(user -> {
            if (checkIfUserIsUnderReviewSync(user.userId)) {
                loginResultState.postValue(new RoleState.Fail("Account is under review"));
            } else {
                loginResultState.postValue(new RoleState.PromptSaveUser(user));
            }
        });

        completableFuture.exceptionally(throwable -> {
            loginResultState.postValue(new RoleState.Fail(throwable.getLocalizedMessage()));
            return null;
        });
    }

    private boolean checkIfUserIsUnderReviewSync (int userId) {
        List<AccountForReview> accountForReviews = appDao.getUserReview(userId);
        return !accountForReviews.isEmpty();
    }

    public void saveUserCredential(int userId, OnOperationSuccessful<Void> onOperationSuccessful) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            appDao.insertSave(new SavedUserCredentials(1, userId));
            return null;
        });

        future.thenAccept(unused -> handler.post(() -> onOperationSuccessful.onSuccess(null)));

        future.exceptionally(throwable -> {
            onOperationSuccessful.onError(throwable.getLocalizedMessage());
            return null;
        });
    }

    public void performRoleBasedRedirect (User user, DummyViewModel.OnUserRoleLoaded onUserRoleLoaded) {
        loginResultState.setValue(new RoleState.Loading());
        CompletableFuture.supplyAsync((Supplier<Void>) () -> {
            List<Admin> admin = appDao.getAdminByUserId(user.userId);
            if (!admin.isEmpty()) {
                onUserRoleLoaded.admin(user, admin.get(0).adminId);
                return null;
            }
            List<Employee> employees = appDao.getEmployeeByUserId(user.userId);
            if (!employees.isEmpty()) {
                onUserRoleLoaded.employee(user, employees.get(0).employeeId);
                return null;
            }
            List<Employer> employers = appDao.getEmployerByUserId(user.userId);
            if (!employers.isEmpty()) {
                onUserRoleLoaded.employer(user, employers.get(0).employerId);
                return null;
            }
            onUserRoleLoaded.noRoleAttached();
            return null;
        });
    }

    public static class RoleState {
        public User user;

        public RoleState(User user) {
            this.user = user;
        }

        public static final class AutoLoginAfterRegistration extends RoleState{

            public AutoLoginAfterRegistration() {
                super(null);
            }
        }

        public static final class AttemptRedirect extends RoleState {

            public AttemptRedirect(User user) {
                super(user);
            }
        }

        public static final class Loading extends RoleState {

            public Loading() {
                super(null);
            }

        }

        public static final class PromptSaveUser extends RoleState{
            public PromptSaveUser(User user) {
                super(user);
            }
        }

        public static final class Fail extends RoleState {
            String message;
            public Fail(String message) {
                super(null);
                this.message = message;
            }
        }

    }

    public interface OnUserReviewStatus {
        void onUserIsNotUnderReview();
        void onUserIsUnderReview();
    }


}