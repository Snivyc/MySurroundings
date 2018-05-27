package com.example.snivy.mysurroundings;

import android.app.Application;

class MyApp extends Application {
    private String account;

    private String password;

    private int ID;

    private String URL;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getURL() {
        return URL;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        URL = getResources().getString(R.string.url);
    }



}