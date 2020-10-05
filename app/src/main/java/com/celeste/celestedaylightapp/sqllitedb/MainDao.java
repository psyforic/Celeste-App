package com.celeste.celestedaylightapp.sqllitedb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao()
public interface MainDao {
    //Insert query
    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    @Delete
    void delete(MainData mainData);

    //Delete all query
    @Delete
    void reset(List<MainData> mainData);

    //Update query
    @Query("UPDATE table_name SET text=:tenantId,text=:userId,text=:role,text=:mode WHERE ID=:sID")
    void update(int sID, String tenantId, String userId, String role, String mode);

    //Get all data query
    @Query("SELECT * FROM table_name")
    List<MainData> getAll();
}
