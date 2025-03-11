package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class JobPostingApplication {
    @PrimaryKey(autoGenerate = true)
    public int jobPostingApplicationId;
    public int jobPostingId;
    public int employeeId;

    public JobPostingApplication(int jobPostingId, int employeeId) {
        this.jobPostingId = jobPostingId;
        this.employeeId = employeeId;
    }
}
