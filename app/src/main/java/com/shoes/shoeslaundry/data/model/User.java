package com.shoes.shoeslaundry.data.model;

public class User {

    private String userId;
    private String namaUser;
    private String emailUser;
    private String nomorHP;

    public User() {

    }

    public User(String userId, String namaUser, String emailUser, String nomorHP) {
        this.userId = userId;
        this.namaUser = namaUser;
        this.emailUser = emailUser;
        this.nomorHP = nomorHP;
    }

    public String getUserId() {
        return userId;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public String getNomorHP() {
        return nomorHP;
    }
}
