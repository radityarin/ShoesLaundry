package com.shoes.shoeslaundry.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Promo implements Parcelable {

    private String title, photo, description, idpromotion;

    public Promo() {
    }

    public Promo(String title, String photo, String description, String idpromotion) {
        this.title = title;
        this.photo = photo;
        this.description = description;
        this.idpromotion = idpromotion;
    }

    public String getTitle() {
        return title;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public String getIdpromotion() {
        return idpromotion;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.photo);
        dest.writeString(this.description);
        dest.writeString(this.idpromotion);
    }

    protected Promo(Parcel in) {
        this.title = in.readString();
        this.photo = in.readString();
        this.description = in.readString();
        this.idpromotion = in.readString();
    }

    public static final Parcelable.Creator<Promo> CREATOR = new Parcelable.Creator<Promo>() {
        @Override
        public Promo createFromParcel(Parcel source) {
            return new Promo(source);
        }

        @Override
        public Promo[] newArray(int size) {
            return new Promo[size];
        }
    };
}
