package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PrivateChatItem {
    @PrimaryKey(autoGenerate = true)
    public int chatId;
    public long timeStampSecond;
    public String timeStamp;
    public int senderId;
    public int threadId;
    public String senderEmail;

    public PrivateChatItem(long timeStampSecond, String timeStamp, int senderId, String senderEmail, int threadId) {
        this.timeStampSecond = timeStampSecond;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.senderEmail = senderEmail;
        this.threadId = threadId;
    }
}
