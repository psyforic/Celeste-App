package com.celeste.celestedaylightapp.sqllitedb;

//Define Table name

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "table_name")
public class MainData implements Serializable {

    //Create ID Column
    @PrimaryKey(autoGenerate = true)
    private int ID;

    //Create text Column
    @ColumnInfo(name = "text")
    private String text;

    //Generate getter and setter

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
