package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(
        childColumns = "userId",
        entity = User.class,
        parentColumns = "userId"
)})
public class Employee {
    @PrimaryKey(autoGenerate = true)
    public int employeeId;
    public int userId;

    public Employee(int userId) {
        this.userId = userId;
    }
}
