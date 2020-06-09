package com.shoes.shoeslaundry.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {

    private String name, ordernumber, idorder, iduser, nohp, droptime, washtype, status, address;
    private int totalprice;

    public Order() {
    }

    public Order(String name, String ordernumber, String idorder, String iduser, String nohp, String droptime, String washtype, String status, String address, int totalprice) {
        this.name = name;
        this.ordernumber = ordernumber;
        this.idorder = idorder;
        this.iduser = iduser;
        this.nohp = nohp;
        this.droptime = droptime;
        this.washtype = washtype;
        this.status = status;
        this.address = address;
        this.totalprice = totalprice;
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

    public String getIdorder() {
        return idorder;
    }

    public void setIdorder(String idorder) {
        this.idorder = idorder;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public static Creator<Order> getCREATOR() {
        return CREATOR;
    }

    protected Order(Parcel in) {
        name = in.readString();
        ordernumber = in.readString();
        idorder = in.readString();
        iduser = in.readString();
        nohp = in.readString();
        droptime = in.readString();
        washtype = in.readString();
        status = in.readString();
        address = in.readString();
        totalprice = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(ordernumber);
        dest.writeString(idorder);
        dest.writeString(iduser);
        dest.writeString(nohp);
        dest.writeString(droptime);
        dest.writeString(washtype);
        dest.writeString(status);
        dest.writeString(address);
        dest.writeInt(totalprice);
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