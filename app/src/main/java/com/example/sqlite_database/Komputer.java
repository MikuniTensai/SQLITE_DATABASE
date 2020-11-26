package com.example.sqlite_database;

public class Komputer {
    private String _id, _nama, harga;
    public Komputer(String id, String nama, String harga) {
        this._id = id;
        this._nama = nama;
        this.harga = harga;
    }
    public Komputer() {
    }
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String get_nama() {
        return _nama;
    }
    public void set_nama(String _nama) {
        this._nama = _nama;
    }
    public String get_harga() {
        return harga;
    }
    public void set_harga(String _harga) {
        this.harga = _harga;
    }
}

