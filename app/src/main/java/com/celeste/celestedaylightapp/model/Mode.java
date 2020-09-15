package com.celeste.celestedaylightapp.model;

import java.io.Serializable;

public class Mode implements Serializable {
    private String name;
    private String command;
    private int drawableResource;
    private boolean sectioned;

    public Mode() {
    }

    public Mode(String name, String command, int drawableResource, boolean sectioned) {
        this.name = name;
        this.command = command;
        this.drawableResource = drawableResource;
        this.sectioned = sectioned;
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
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isSectioned() {
        return sectioned;
    }
}
