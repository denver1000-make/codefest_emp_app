package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(
        entity = User.class,
        childColumns = "userId",
        parentColumns = "userId"
)})
public class Admin {
    @PrimaryKey(autoGenerate = true)
    public int adminId;
    public int userId;

    public Admin(int userId) {
        this.userId = userId;
    }
}
