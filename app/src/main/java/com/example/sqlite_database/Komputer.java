package com.example.sqlite_database;

public class Komputer {
    private String _id, _nama, _kelas;
    public Komputer(String id, String nama, String kelas) {
        this._id = id;
        this._nama = nama;
        this._kelas = kelas;
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
    public String get_kelas() {
        return _kelas;
    }
    public void set_kelas(String _kelas) {
        this._kelas = _kelas;
    }
}
