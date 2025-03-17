package com.denprog.codefestapp.room;


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
import com.denprog.codefestapp.room.entity.PrivateChatItem;
import com.denprog.codefestapp.room.entity.PrivateChatItemText;
import com.denprog.codefestapp.room.entity.PrivateChatThread;
import com.denprog.codefestapp.room.entity.ReviewStatus;
import com.denprog.codefestapp.room.entity.SavedUserCredentials;
import com.denprog.codefestapp.room.entity.User;
import com.denprog.codefestapp.room.view.ChatThreadWithEmployeeName;
import com.denprog.codefestapp.room.view.JobPostingApplicationAndEmployeeInfo;

@Database(version = 9, entities = {
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
        JobPostingApplication.class,
        PrivateChatThread.class,
        PrivateChatItemText.class,
        PrivateChatItem.class},
        views = {JobPostingApplicationAndEmployeeInfo.class, ChatThreadWithEmployeeName.class},
        exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao getAppDao();

}
