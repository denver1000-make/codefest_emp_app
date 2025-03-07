package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = User.class,
                parentColumns = "userId",
                childColumns = "userId"
        )
})
public class Employer {
    public int userId;
    @PrimaryKey(autoGenerate = true)
    public int employerId;

    public Employer(int userId) {
        this.userId = userId;
    }
}
