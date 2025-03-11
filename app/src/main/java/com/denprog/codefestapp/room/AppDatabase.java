package com.denprog.codefestapp.room;


import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.denprog.codefestapp.room.dao.AppDao;
import com.denprog.codefestapp.room.entity.AccountForReview;
import com.denprog.codefestapp.room.entity.Admin;
import com.denprog.codefestapp.room.entity.Credentials;
import com.denprog.codefestapp.room.entity.Employee;
import com.denprog.codefestapp.room.entity.Employer;
import com.denprog.codefestapp.room.entity.JobPosting;
import com.denprog.codefestapp.room.entity.JobPostingApplication;
import com.denprog.codefestapp.room.entity.JobPostingApplicationFile;
import com.denprog.codefestapp.room.entity.ReviewStatus;
import com.denprog.codefestapp.room.entity.SavedUserCredentials;
import com.denprog.codefestapp.room.entity.User;

@Database(version = 1, entities = {
        User.class,
        Employer.class,
        Employee.class,
        JobPosting.class,
        Admin.class,
        Credentials.class,
        AccountForReview.class,
        ReviewStatus.class,
        SavedUserCredentials.class,
        JobPostingApplicationFile.class,
        JobPostingApplication.class},
        exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao getAppDao();

}
