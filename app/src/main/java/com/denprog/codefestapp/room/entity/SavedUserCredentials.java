package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SavedUserCredentials {

    @PrimaryKey
    public int saveId;
    public int userId;

    public SavedUserCredentials(int saveId, int userId) {
        this.saveId = saveId;
        this.userId = userId;
    }
}
