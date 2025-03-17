package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = Employer.class,
                        parentColumns = "employerId",
                        childColumns = "employerId")   ,
                @ForeignKey(
                        entity = Employee.class,
                        parentColumns = "employeeId",
                        childColumns = "employeeId"
                )
        }
)
public class PrivateChatThread {
    @PrimaryKey(autoGenerate = true)
    public int threadId;
    public int employeeId;
    public int employerId;

    public PrivateChatThread(int employeeId, int employerId) {
        this.employeeId = employeeId;
        this.employerId = employerId;
    }
}
