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

    public PrivateChatItem(long timeStampSecond, String timeStamp, int senderId, int threadId) {
        this.timeStampSecond = timeStampSecond;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.threadId = threadId;
    }
}
