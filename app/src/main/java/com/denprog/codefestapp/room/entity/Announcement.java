package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Announcement {
    @PrimaryKey(autoGenerate = true)
    public int announcementId;
    public int adminId;
    public String announcementName;
    public String announcementDescription;

    public Announcement(int adminId, String announcementName, String announcementDescription) {
        this.adminId = adminId;
        this.announcementName = announcementName;
        this.announcementDescription = announcementDescription;
    }
}
