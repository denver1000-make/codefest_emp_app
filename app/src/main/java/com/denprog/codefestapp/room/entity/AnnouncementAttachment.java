package com.denprog.codefestapp.room.entity;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import javax.annotation.Nullable;

@Entity
public class AnnouncementAttachment {

    @Ignore
    public static final String ANNOUNCEMENT_ATTACHMENT_FOLDER = "announce_attachments";

    @PrimaryKey(autoGenerate = true)
    public int attachmentId;
    public int announcementId;
    public String filePath;

    @Nullable
    @Ignore
    public Uri uri = null;
    @Ignore
    public String fileName;


    public AnnouncementAttachment() {
    }

    public AnnouncementAttachment(String filePath) {
        this.filePath = filePath;
    }

    @Ignore
    public AnnouncementAttachment(String filePath, Uri uri) {
        this.filePath = filePath;
        this.uri = uri;
    }
}
