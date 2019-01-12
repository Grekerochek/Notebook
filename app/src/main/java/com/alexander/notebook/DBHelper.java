package com.alexander.notebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION_DB = 1;
    private static final String DB_NAME = "database";

    public DBHelper(Context context){
        this(context, DB_NAME, null, VERSION_DB);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createEmptyTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db);
        onCreate(db);
    }

    private void createEmptyTables(SQLiteDatabase db) {
        db.execSQL("create table NOTES(id integer primary key, title text, text text, textSize integer, textColor integer)");
    }

    private void deleteTables(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS NOTES");
    }
}
