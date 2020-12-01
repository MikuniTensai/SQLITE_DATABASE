package com.example.sqlite_database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper_Item extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DB_Login";
    public static final String TABLE_NAME = "TB_Login";
    public static final String COL_1 = "Username";
    public static final String COL_2 = "Password";
    private SQLiteDatabase db;
    public DBHelper_Item(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (Username STRING PRIMARY KEY, Password TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void SimpanData(String Username, String Password) {
        ContentValues values = new ContentValues();
        values.put(COL_1, Username);
        values.put(COL_2, Password);
        db.insert(TABLE_NAME, null, values);
    }
    public Cursor LihatData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME, null);
        return res;
    }
    public void HapusData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }
}
