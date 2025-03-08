package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(
        childColumns = "userId",
        parentColumns = "userId",
        entity = User.class
)})
public class Credentials {
    @PrimaryKey(autoGenerate = true)
    public int credentialId;
    public String pathToFile;
    public int userId;
    public Credentials(int userId, String pathToFile) {
        this.userId = userId;
        this.pathToFile = pathToFile;
    }
}
