package com.celeste.celestedaylightapp.sqllitedb;

//add database entities

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={MainData.class},version = 1,exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    //Create database instance
    private static RoomDB database;

    //Define db name
    private static String DATABASE_NAME="database";

    public synchronized static RoomDB getInstance(Context context)
    {
        if(database==null)
        {
            //When database is null
            //Initialize database
            database= Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        //Return database
        return database;
    }

    //Create Dao

    public abstract MainDao mainDao();

}