package com.shoes.shoeslaundry.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {

    private String nama, iduser, namasepatu, ukuransepatu, warnasepatu, tanggaldrop, status;

    public Order() {
    }

    public Order(String nama, String iduser, String namasepatu, String ukuransepatu, String warnasepatu, String tanggaldrop, String status) {
        this.nama = nama;
        this.iduser = iduser;
        this.namasepatu = namasepatu;
        this.ukuransepatu = ukuransepatu;
        this.warnasepatu = warnasepatu;
        this.tanggaldrop = tanggaldrop;
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getNamasepatu() {
        return namasepatu;
    }

    public void setNamasepatu(String namasepatu) {
        this.namasepatu = namasepatu;
    }

    public String getUkuransepatu() {
        return ukuransepatu;
    }

    public void setUkuransepatu(String ukuransepatu) {
        this.ukuransepatu = ukuransepatu;
    }

    public String getWarnasepatu() {
        return warnasepatu;
    }

    public void setWarnasepatu(String warnasepatu) {
        this.warnasepatu = warnasepatu;
    }

    public String getTanggaldrop() {
        return tanggaldrop;
    }

    public void setTanggaldrop(String tanggaldrop) {
        this.tanggaldrop = tanggaldrop;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    protected Order(Parcel in) {
        nama = in.readString();
        iduser = in.readString();
        namasepatu = in.readString();
        ukuransepatu = in.readString();
        warnasepatu = in.readString();
        tanggaldrop = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama);
        dest.writeString(iduser);
        dest.writeString(namasepatu);
        dest.writeString(ukuransepatu);
        dest.writeString(warnasepatu);
        dest.writeString(tanggaldrop);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}