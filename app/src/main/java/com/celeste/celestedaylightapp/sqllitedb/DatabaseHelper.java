package com.celeste.celestedaylightapp.sqllitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.celeste.celestedaylightapp.model.User;
import com.celeste.celestedaylightapp.model.modes.Mode;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "USER_MODES";
    public static final String TABLE_USER = "USER_INFO";
    private static final String DATABASE_NAME = "UserManager.db";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MODE_ID = "mode_id";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_CMD = "command";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";
    static final String DB_NAME = "USER_MODES.DB";
    static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_START_TIME + " TEXT, " +
            COLUMN_END_TIME + " TEXT, " +
            COLUMN_DESC + " TEXT, " +
            COLUMN_CMD + " TEXT, " +
            COLUMN_ICON + " TEXT, " +
            COLUMN_MODE_ID + " TEXT" + ")";
//    private static final String COLUMN_USER_ID = "user_id";
//    private static final String COLUMN_USER_NAME = "user_name";
//    private static final String COLUMN__NAME = "user_name";
//    private static final String COLUMN_USER_EMAIL = "user_email";
//    private static final String COLUMN_USER_PASSWORD = "user_password";
//    // create table sql query
//    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
//            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//            + COLUMN_USER_NAME + " TEXT,"
//            + COLUMN_USER_NAME + " TEXT,"
//            + COLUMN_USER_EMAIL + " TEXT,"
//            + COLUMN_USER_PASSWORD + " TEXT" + ")";
//    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public boolean isFieldExists(SQLiteDatabase db, String tableName, String fieldName) {
        if (tableName == null || db == null || fieldName == null || !db.isOpen()) return false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0", null);
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
    public void addUser(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_USER_NAME, user.getTenantName());
//        values.put(COLUMN_USER_EMAIL, user.get());
//        values.put(COLUMN_USER_PASSWORD, user.getPassword());
//        // Inserting Row
//        db.insert(TABLE_USER, null, values);
//        db.close();
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
        Mode mode = new Mode();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USER_MODES ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //arrayList.add(cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));
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
        Cursor cursor = db.rawQuery("SELECT EXISTS(SELECT 1 FROM USER_MODES WHERE COLUMN_DESC  = " + desc + "", null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean fieldExists(SQLiteDatabase db, String tableName, String fieldName) {
        boolean doesExist = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("Select * from " + tableName + "limit 1", null);
            int colIndex = cursor.getColumnIndex(fieldName);
            if (colIndex != -1) {
                doesExist = true;
            }
        } catch (Exception e) {
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception ex) {

            }
        }
        return doesExist;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
