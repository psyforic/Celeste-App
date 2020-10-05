package com.celeste.celestedaylightapp.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.celeste.celestedaylightapp.sqllitedb.DatabaseHelper;
import com.sdsmdg.tastytoast.TastyToast;

public class CelesteService extends Service {
    private final IBinder mBinder = new MyBinder();
    Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public CelesteService() {
    }

    public CelesteService(DatabaseHelper sqlDB) {
        this.dbHelper = sqlDB;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        dbHelper = new DatabaseHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();
        super.onCreate();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isConnected = this.isNetworkConnected();
        if (!isConnected) {
            TastyToast.makeText(getApplicationContext(), "Some information may not appear because you are offline, please sync when you have internet access", TastyToast.LENGTH_LONG, TastyToast.INFO).show();
        } else {
            sqLiteDatabase = dbHelper.getReadableDatabase();
            dbHelper.getAllModes();
            TastyToast.makeText(getApplicationContext(), "" + isConnected, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MyBinder extends Binder {
        CelesteService getService() {
            return CelesteService.this;

        }

    }
}
