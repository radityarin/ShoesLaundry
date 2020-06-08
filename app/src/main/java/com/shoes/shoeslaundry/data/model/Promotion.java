package com.shoes.shoeslaundry.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Promotion implements Parcelable {

    private String title, photo, description, idpromotion;

    public Promotion() {
    }

    public Promotion(String title, String photo, String description, String idpromotion) {
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

    protected Promotion(Parcel in) {
        this.title = in.readString();
        this.photo = in.readString();
        this.description = in.readString();
        this.idpromotion = in.readString();
    }

    public static final Parcelable.Creator<Promotion> CREATOR = new Parcelable.Creator<Promotion>() {
        @Override
        public Promotion createFromParcel(Parcel source) {
            return new Promotion(source);
        }

        @Override
        public Promotion[] newArray(int size) {
            return new Promotion[size];
        }
    };
}
