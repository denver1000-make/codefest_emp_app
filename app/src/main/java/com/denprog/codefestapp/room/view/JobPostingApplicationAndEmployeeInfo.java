package com.denprog.codefestapp.room.view;

import androidx.room.DatabaseView;

@DatabaseView(
        "SELECT firstName, lastName, middleName, email, Employee.employeeId, jobPostingApplicationId, jobPostingId FROM JobPostingApplication " +
        "INNER JOIN Employee " +
        "ON JobPostingApplication.employeeId = Employee.employeeId " +
        "INNER JOIN User " +
        "ON User.userId = Employee.userId")
public class JobPostingApplicationAndEmployeeInfo {

    public String firstName;
    public String middleName;
    public String lastName;
    public String email;
    public int employeeId;
    public int jobPostingApplicationId;
    public int jobPostingId;



}
