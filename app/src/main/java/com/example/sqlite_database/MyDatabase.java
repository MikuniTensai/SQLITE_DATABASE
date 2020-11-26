package com.example.sqlite_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 1;
    //nama, processor, ram, storage, vga, monitor, psu, hsf, os, harga;
    private static final String DATABASE_NAME = "db_rk";
    private static final String tb_komputer = "tb_komputer";
    private static final String tb_komputer_id = "id";
    private static final String tb_komputer_gambar = "gambar";
    private static final String tb_komputer_nama = "nama";
    private static final String tb_komputer_harga = "harga";
    private static final String CREATE_TABLE_KOMPUTER= "CREATE TABLE " +
            tb_komputer + "("
            + tb_komputer_id + " INTEGER PRIMARY KEY ,"
            + tb_komputer_nama + " TEXT,"
            + tb_komputer_harga + " TEXT " + ")";

    public MyDatabase (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_KOMPUTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void CreateKomputer (Komputer mdNotif) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(tb_komputer_id, mdNotif.get_id());
        values.put(tb_komputer_nama, mdNotif.get_nama());
        values.put(tb_komputer_harga, mdNotif.get_harga());
        db.insert(tb_komputer, null, values);
        db.close();
    }

    public List<Komputer> ReadKomputer() {
        List<Komputer> judulModelList = new ArrayList<Komputer>();
        String selectQuery = "SELECT * FROM " + tb_komputer;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Komputer mdKontak = new Komputer();
                mdKontak.set_id(cursor.getString(0));
                mdKontak.set_nama(cursor.getString(1));
                mdKontak.set_harga(cursor.getString(2));
                judulModelList.add(mdKontak);
            } while (cursor.moveToNext());
        }
        db.close();
        return judulModelList;
    }
    public int UpdateKomputer (Komputer mdNotif) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(tb_komputer_nama, mdNotif.get_nama());
        values.put(tb_komputer_harga, mdNotif.get_harga());
        return db.update(tb_komputer, values, tb_komputer_id + " = ?",
                new String[] { String.valueOf(mdNotif.get_id())});
    }
    public void DeleteKomputer (Komputer mdNotif) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_komputer, tb_komputer_id+ " = ?",
                new String[]{String.valueOf(mdNotif.get_id())});
        db.close();
    }
}
