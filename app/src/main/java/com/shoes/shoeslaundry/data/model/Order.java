package com.shoes.shoeslaundry.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {

    private String name, ordernumber, iduser, nohp, droptime, washtype, status, address;
    private int totalprice;

    public Order() {
    }

    public Order(String name, String ordernumber, String iduser, String nohp, String droptime, String washtype, String status, String address, int totalprice) {
        this.name = name;
        this.ordernumber = ordernumber;
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

    public String getOrdernumber() {
        return ordernumber;
    }

    public String getIduser() {
        return iduser;
    }

    public String getNohp() {
        return nohp;
    }

    public String getDroptime() {
        return droptime;
    }

    public String getWashtype() {
        return washtype;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public static Creator<Order> getCREATOR() {
        return CREATOR;
    }

    protected Order(Parcel in) {
        name = in.readString();
        ordernumber = in.readString();
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