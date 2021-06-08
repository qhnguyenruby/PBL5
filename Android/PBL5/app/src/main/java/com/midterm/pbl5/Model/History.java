package com.midterm.pbl5.Model;

import java.sql.Timestamp;

public class History {

    private int id;
    private float luongnuoc;
    private String loaidat;
    private String chedo;
    private Timestamp thoigian;

    public History(int id, float luongnuoc, String loaidat, String chedo, Timestamp thoigian) {
        this.id = id;
        this.luongnuoc = luongnuoc;
        this.loaidat = loaidat;
        this.chedo = chedo;
        this.thoigian = thoigian;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLuongnuoc() {
        return luongnuoc;
    }

    public void setLuongnuoc(float luongnuoc) {
        this.luongnuoc = luongnuoc;
    }

    public String getLoaidat() {
        return loaidat;
    }

    public void setLoaidat(String loaidat) {
        this.loaidat = loaidat;
    }

    public String getChedo() {
        return chedo;
    }

    public void setChedo(String chedo) {
        this.chedo = chedo;
    }

    public Timestamp getThoigian() {
        return thoigian;
    }

    public void setThoigian(Timestamp thoigian) {
        this.thoigian = thoigian;
    }
}
