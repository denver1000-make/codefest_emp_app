package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = Employee.class,
                parentColumns = "employeeId",
                childColumns = "employeeId"
        )
})
public class JobPostingApplicationFile {
    @Ignore
    public static final String FOLDER_OF_APPLICATION_FILE = "application_files";
    @PrimaryKey(autoGenerate = true)
    public int applicationId;
    public String pathOfFile;
    public int employeeId;

    public JobPostingApplicationFile(String pathOfFile) {
        this.pathOfFile = pathOfFile;
    }
}
