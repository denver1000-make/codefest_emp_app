package com.denprog.codefestapp.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.denprog.codefestapp.room.entity.Admin;
import com.denprog.codefestapp.room.entity.Credentials;
import com.denprog.codefestapp.room.entity.Employee;
import com.denprog.codefestapp.room.entity.Employer;
import com.denprog.codefestapp.room.entity.User;

import java.util.List;

@Dao
public interface AppDao {
    @Insert
    long insertUser(User user);
    @Insert
    long insertEmployee(Employee employee);
    @Insert
    void insertEmployer(Employer employer);
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

}
