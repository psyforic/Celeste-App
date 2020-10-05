package com.celeste.celestedaylightapp.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    public Drawable imageDrw;
    private String userId;
    private String tenantName;
    private String username;
    private String firstName;
    private String lastName;
    private String userEmail;
    private List<Mode> userModes;

    public List<Mode> getUserModes() {
        return userModes;
    }

    public void setUserModes(List<Mode> userModes) {
        this.userModes = userModes;
    }

    private int userProfileImage;

    public User() {
    }

    public User(String userId, String tenantName, String username, String userFirstName, String userLastName, String userEmail, int userProfileImage) {
        this.userId = userId;
        this.tenantName = tenantName;
        this.username = username;
        this.firstName = userFirstName;
        this.lastName = userLastName;
        this.userEmail = userEmail;
        this.userProfileImage = userProfileImage;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
