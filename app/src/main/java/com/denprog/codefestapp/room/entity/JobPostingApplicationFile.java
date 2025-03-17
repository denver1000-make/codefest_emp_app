package com.denprog.codefestapp.room.entity;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = Employee.class,
                parentColumns = "employeeId",
                childColumns = "employeeId"
        ),
        @ForeignKey(
                entity = JobPostingApplication.class,
                childColumns = "jobPostingApplicationId",
                parentColumns = "jobPostingApplicationId"
        )
})
public class JobPostingApplicationFile {
    @Ignore
    public static final String FOLDER_OF_APPLICATION_FILE = "application_files";
    @PrimaryKey(autoGenerate = true)
    public int jobPostingApplicationFileId;
    public String pathOfFile;
    public int employeeId;
    public int jobPostingApplicationId;
    @Ignore
    public Uri uri;

    @Ignore
    public JobPostingApplicationFile(String pathOfFile, int employeeId, int jobPostingApplicationId) {
        this.pathOfFile = pathOfFile;
        this.employeeId = employeeId;
        this.jobPostingApplicationId = jobPostingApplicationId;
    }

    @Ignore
    public JobPostingApplicationFile(String pathOfFile) {
        this.pathOfFile = pathOfFile;
    }

    public JobPostingApplicationFile() {

    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
