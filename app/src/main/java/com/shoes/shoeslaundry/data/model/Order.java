package com.shoes.shoeslaundry.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {

    private String name, ordernumber, iduser, shoesname, shoessize, shoescolour, droptime, washtype, status;

    public Order() {
    }

    public Order(String name, String ordernumber, String iduser, String shoesname, String shoessize, String shoescolour, String droptime, String washtype, String status) {
        this.name = name;
        this.ordernumber = ordernumber;
        this.iduser = iduser;
        this.shoesname = shoesname;
        this.shoessize = shoessize;
        this.shoescolour = shoescolour;
        this.droptime = droptime;
        this.washtype = washtype;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getShoesname() {
        return shoesname;
    }

    public void setShoesname(String shoesname) {
        this.shoesname = shoesname;
    }

    public String getShoessize() {
        return shoessize;
    }

    public void setShoessize(String shoessize) {
        this.shoessize = shoessize;
    }

    public String getShoescolour() {
        return shoescolour;
    }

    public void setShoescolour(String shoescolour) {
        this.shoescolour = shoescolour;
    }

    public String getDroptime() {
        return droptime;
    }

    public void setDroptime(String droptime) {
        this.droptime = droptime;
    }

    public String getWashtype() {
        return washtype;
    }

    public void setWashtype(String washtype) {
        this.washtype = washtype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Creator<Order> getCREATOR() {
        return CREATOR;
    }

    protected Order(Parcel in) {
        name = in.readString();
        ordernumber = in.readString();
        iduser = in.readString();
        shoesname = in.readString();
        shoessize = in.readString();
        shoescolour = in.readString();
        droptime = in.readString();
        washtype = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(ordernumber);
        dest.writeString(iduser);
        dest.writeString(shoesname);
        dest.writeString(shoessize);
        dest.writeString(shoescolour);
        dest.writeString(droptime);
        dest.writeString(washtype);
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