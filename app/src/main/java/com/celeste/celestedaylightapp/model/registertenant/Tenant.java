package com.celeste.celestedaylightapp.model.registertenant;

public class Tenant {
    private String tenancyName;
    private String name;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String mail;
    private String connectionString;
    private Boolean isActive;
    public Tenant()
    {

    }
    public Tenant(String tenancyName, String name, String firstName, String lastName, String password, String email, String connectionString, Boolean isActive, String mail) {
        this.tenancyName = tenancyName;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.isActive = isActive;
        this.connectionString = "";
        this.mail = mail;
    }
    public String getTenancyName() {
        return tenancyName;
    }

    public void setTenancyName(String tenancyName) {
        this.tenancyName = tenancyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
