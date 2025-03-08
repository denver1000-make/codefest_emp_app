package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(
        entity = User.class,
        childColumns = "userId",
        parentColumns = "userId"
)})
public class AccountForReview {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;

    public AccountForReview(int userId) {
        this.userId = userId;
    }

}
