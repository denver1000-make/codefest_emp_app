package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(
                childColumns = "employerId",
                parentColumns = "employerId",
                entity = Employer.class
        )
})
public class JobPosting {
    @PrimaryKey(autoGenerate = true)
    public int postingId;
    public String postingName;
    public String postingDescription;
    public String postingCategory;
    public float maxSalary;
    public float minSalary;
    public int employerId;

    public JobPosting(String postingName, String postingDescription, String postingCategory, float minSalary, float maxSalary, int employerId) {
        this.postingName = postingName;
        this.postingDescription = postingDescription;
        this.postingCategory = postingCategory;
        this.maxSalary = maxSalary;
        this.minSalary = minSalary;
        this.employerId = employerId;
    }
}
