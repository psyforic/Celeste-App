package com.celeste.celestedaylightapp.sqllitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.celeste.celestedaylightapp.model.modes.Mode;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "USER_MODES";
    public static final String TABLE_USER = "USER_INFO";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MODE_ID = "mode_id";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_CMD = "command";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";
    static final String DB_NAME = "USER_MODES.DB";
    static final int DB_VERSION = 1;
    private static final String DATABASE_NAME = "UserManager.db";
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_START_TIME + " TEXT, " +
            COLUMN_END_TIME + " TEXT, " +
            COLUMN_DESC + " TEXT, " +
            COLUMN_CMD + " TEXT, " +
            COLUMN_ICON + " TEXT, " +
            COLUMN_MODE_ID + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public boolean isFieldExists(SQLiteDatabase db, String tableName, String fieldName) {
        if (tableName == null || db == null || fieldName == null || !db.isOpen())
            return false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + "  LIMIT 0", null);
            return cursor != null && cursor.getColumnIndex(fieldName) != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public boolean insertUserMode(String start_time, String end_time, String description, String command, String icon, String modeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("start_time", start_time);
        contentValues.put("end_time", end_time);
        contentValues.put("description", description);
        contentValues.put("command", command);
        contentValues.put("icon", icon);
        contentValues.put("mode_id", modeId);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean insertUserMode(Mode mode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("start_time", mode.getStartTime());
        contentValues.put("end_time", mode.getEndTime());
        contentValues.put("description", mode.getName());
        contentValues.put("command", mode.getCommand());
        contentValues.put("icon", mode.getIcon());
        contentValues.put("mode_id", mode.getId());
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from USER_MODES where id=" + id + "", null);
        cursor.close();
        return cursor;
    }

    public List<Mode> getAllModes() {
        List<Mode> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USER_MODES ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Mode mode = new Mode();
            mode.setName(cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));
            mode.setCommand(cursor.getString(cursor.getColumnIndex(COLUMN_CMD)));
            mode.setStartTime(cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME)));
            mode.setEndTime(cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME)));
            mode.setId(cursor.getString(cursor.getColumnIndex(COLUMN_MODE_ID)));
            arrayList.add(mode);
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public boolean recordExists(String desc) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT EXISTS (SELECT * FROM USER_MODES WHERE description='" + desc + "' LIMIT 1)";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        // cursor.getInt(0) is 1 if column with value exists
        if (cursor.getInt(0) == 1) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select 1 from USER_MODES where description=" + desc + "", null);
//        if (cursor.getCount() <= 0) {
//            cursor.close();
//            return false;
//        }
//        cursor.close();
//        return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
