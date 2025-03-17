package com.denprog.codefestapp.room.view;

import androidx.room.DatabaseView;

@DatabaseView("SELECT PrivateChatThread.threadId, User.firstName, User.middleName, User.lastName, User.email, Employer.employerId, PrivateChatThread.employeeId FROM PrivateChatThread INNER JOIN Employer ON PrivateChatThread.employerId = Employer.employerId INNER JOIN User ON Employer.userId = User.userId")
public class ChatThreadWithEmployeeName {
    public int employerId;
    public int employeeId;
    public String firstName;
    public int threadId;
    public String lastName;
    public String middleName;
}
