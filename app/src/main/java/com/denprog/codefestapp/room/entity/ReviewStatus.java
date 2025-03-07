package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(
        entity = User.class,
        childColumns = "userId",
        parentColumns = "userId"
)})
public class ReviewStatus {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public boolean statusOfAccount;
    public int userId;

    public ReviewStatus(boolean statusOfAccount, int userId) {
        this.statusOfAccount = statusOfAccount;
        this.userId = userId;
    }

}
