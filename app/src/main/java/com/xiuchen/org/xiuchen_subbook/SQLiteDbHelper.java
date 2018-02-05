package com.xiuchen.org.xiuchen_subbook;

/*
    This class should not be used. But I could not think of another way to do this. This class helps MainActivity to edit subscription and
    delete subscription and save the new record.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDbHelper extends SQLiteOpenHelper {

    private static Integer version = 1;
    private static String name = "test_db";

    public SQLiteDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteDbHelper(Context context) {
        this(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("SQLiteDbHelper", "Create database and table start.");
        String sql = "create table record(id integer primary key autoincrement, name varchar(128), charge double, date datetime, comments varchar(256))";
        db.execSQL(sql);
        Log.d("SQLiteDbHelper", "Create database and table finish.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
