package com.denprog.codefestapp.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(
        childColumns = "employeeId",
        parentColumns = "employeeId",
        entity = Employee.class
)})
public class Credentials {

    @PrimaryKey(autoGenerate = true)
    public int credentialId;
    public String pathToFile;
    public int employeeId;

    public Credentials(int employeeId, String pathToFile) {
        this.employeeId = employeeId;
        this.pathToFile = pathToFile;
    }
}
