package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int userId;
    public String firstName;
    public String middleName;
    public String lastName;
    public String password;
    public String email;
    public String roleName;

    public User(String firstName, String lastName, String middleName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.password = password;
        this.email = email;
    }
}
