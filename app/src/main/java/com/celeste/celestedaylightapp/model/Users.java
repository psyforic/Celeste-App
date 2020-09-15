package com.celeste.celestedaylightapp.model;

import android.graphics.drawable.Drawable;

public class Users {
    public int image;
    public Drawable imageDrw;
    public String name;
    public String email;
    public boolean section = false;

    public Users() {
    }

    public Users(String name, boolean section) {
        this.name = name;
        this.section = section;
    }
}
