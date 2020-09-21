package com.celeste.celestedaylightapp.model.user;

import java.io.Serializable;

public class UserModes implements Serializable {
    private String name;
    private String time;
    private int drawableResource;

    public UserModes() {
    }

    public UserModes(String name, String time, int drawableResource, boolean sectioned) {
        this.name = name;
        this.time = time;
        this.drawableResource = drawableResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawableResource() {
        return drawableResource;
    }

    public void setDrawableResource(int drawableResource) {
        this.drawableResource = drawableResource;
    }

    public String getCommand() {
        return time;
    }

    public void setCommand(String command) {
        this.time = command;
    }



}
