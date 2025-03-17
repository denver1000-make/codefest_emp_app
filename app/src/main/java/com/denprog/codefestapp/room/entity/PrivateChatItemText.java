package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;

@Entity
public final class PrivateChatItemText extends PrivateChatItem{
    public String chatContent;

    public PrivateChatItemText(long timeStampSecond, String timeStamp, int senderId, String senderEmail, int threadId, String chatContent) {
        super(timeStampSecond, timeStamp, senderId, senderEmail, threadId);
        this.chatContent = chatContent;
    }
}
