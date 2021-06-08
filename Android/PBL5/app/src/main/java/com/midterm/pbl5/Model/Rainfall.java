package com.midterm.pbl5.Model;

public class Rainfall {
    private int id;
    private float luongmuathucte;
    private float luongmuadudoan;
    private String thoigian;

    public Rainfall(int id, float luongmuathucte, float luongmuadudoan, String thoigian) {
        this.id = id;
        this.luongmuathucte = luongmuathucte;
        this.luongmuadudoan = luongmuadudoan;
        this.thoigian = thoigian;
    }

    public Rainfall() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLuongmuathucte() {
        return luongmuathucte;
    }

    public void setLuongmuathucte(float luongmuathucte) {
        this.luongmuathucte = luongmuathucte;
    }

    public float getLuongmuadudoan() {
        return luongmuadudoan;
    }

    public void setLuongmuadudoan(float luongmuadudoan) {
        this.luongmuadudoan = luongmuadudoan;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }
}
