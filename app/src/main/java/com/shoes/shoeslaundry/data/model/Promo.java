package com.shoes.shoeslaundry.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Promo implements Parcelable {

    private String title, photo, description, idpromotion, startdate, enddate;

    public Promo() {
    }

    public Promo(String title, String photo, String description, String idpromotion, String startdate, String enddate) {
        this.title = title;
        this.photo = photo;
        this.description = description;
        this.idpromotion = idpromotion;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdpromotion() {
        return idpromotion;
    }

    public void setIdpromotion(String idpromotion) {
        this.idpromotion = idpromotion;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public static Creator<Promo> getCREATOR() {
        return CREATOR;
    }

    protected Promo(Parcel in) {
        title = in.readString();
        photo = in.readString();
        description = in.readString();
        idpromotion = in.readString();
        startdate = in.readString();
        enddate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(photo);
        dest.writeString(description);
        dest.writeString(idpromotion);
        dest.writeString(startdate);
        dest.writeString(enddate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Promo> CREATOR = new Creator<Promo>() {
        @Override
        public Promo createFromParcel(Parcel in) {
            return new Promo(in);
        }

        @Override
        public Promo[] newArray(int size) {
            return new Promo[size];
        }
    };
}
