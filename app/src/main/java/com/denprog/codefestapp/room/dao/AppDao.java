package com.denprog.codefestapp.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.denprog.codefestapp.room.entity.AccountForReview;
import com.denprog.codefestapp.room.entity.Admin;
import com.denprog.codefestapp.room.entity.Credentials;
import com.denprog.codefestapp.room.entity.Employee;
import com.denprog.codefestapp.room.entity.Employer;
import com.denprog.codefestapp.room.entity.JobPosting;
import com.denprog.codefestapp.room.entity.ReviewStatus;
import com.denprog.codefestapp.room.entity.SavedUserCredentials;
import com.denprog.codefestapp.room.entity.User;

import java.util.List;

@Dao
public interface AppDao {
    @Insert
    long insertUser(User user);
    @Insert
    long insertEmployee(Employee employee);
    @Insert
    long insertEmployer(Employer employer);
    @Insert
    void insertAdmin(Admin admin);
    @Insert
    void insertCredential(Credentials credentials);
    @Query("SELECT * FROM User WHERE User.email = :email")
    List<User> checkIfEmailAlreadyExist(String email);
    @Query("SELECT * FROM User WHERE User.email = :email AND User.password = :password")
    List<User> getUserByEmailAndPassword(String email, String password);
    @Query("SELECT * FROM Admin WHERE userId = :userId")
    List<Admin> getAdminByUserId(int userId);
    @Query("SELECT * FROM Employee WHERE userId = :userId")
    List<Employee> getEmployeeByUserId(int userId);
    @Query("SELECT * FROM Employer WHERE userId = :userId")
    List<Employer> getEmployerByUserId(int userId);
    @Query("SELECT * FROM Credentials WHERE userId = :userId")
    List<Credentials> getAllCredentialsByUserIdCredentials(int userId);
    @Insert
    void insertAccountReview(AccountForReview accountForReview);
    @Query("SELECT User.userId, User.email, User.password, User.firstName, User.middleName, User.lastName FROM User INNER JOIN AccountForReview ON User.userId = AccountForReview.userId")
    List<User> getAllAccountsForReview();
    @Query("SELECT * FROM User WHERE User.userId = :userId")
    List<User> getUserById(int userId);
    @Query("DELETE FROM AccountForReview WHERE userId=:userId")
    void removeUserFromReview(int userId);
    @Insert
    void insertReviewStatus(ReviewStatus reviewStatus);
    @Query("SELECT * FROM AccountForReview WHERE userId=:userId")
    List<AccountForReview> getUserReview(int userId);
    @Query("SELECT * FROM JobPosting")
    List<JobPosting> getAllJobPosting();
    @Insert
    long insertJobPosting(JobPosting jobPosting);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSave(SavedUserCredentials savedUserCredentials);
    @Query("SELECT * FROM SavedUserCredentials")
    List<SavedUserCredentials> getAllSavedUserCredentials();
}
