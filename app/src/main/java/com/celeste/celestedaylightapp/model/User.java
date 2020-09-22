package com.celeste.celestedaylightapp.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class User implements Serializable {
    public Drawable imageDrw;
    private String userId;
    private String firstName;
    private String lastName;
    private String userEmail;
    private int userProfileImage;

    public User() {
    }


    public User(String userId, String userFirstName, String userLastName, String userEmail, int userProfileImage) {
        this.userId = userId;
        this.firstName = userFirstName;
        this.lastName = userLastName;
        this.userEmail = userEmail;
        this.userProfileImage = userProfileImage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(int userProfileImage) {
        this.userProfileImage = userProfileImage;
    }


}
