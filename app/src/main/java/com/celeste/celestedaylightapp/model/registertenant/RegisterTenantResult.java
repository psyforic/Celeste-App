package com.celeste.celestedaylightapp.model.registertenant;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tenancyName",
        "name",
        "firstName",
        "lastName",
        "password",
        "adminEmailAddress",
        "connectionString",
        "isActive",
        "email"
})
public class RegisterTenantResult {
    @JsonProperty("tenancyName")
    private String tenancyName;
    @JsonProperty("name")
    private String name;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("password")
    private String password;
    @JsonProperty("adminEmailAddress")
    private String adminEmailAddress;
    @JsonProperty("connectionString")
    private String connectionString;
    @JsonProperty("isActive")
    private Boolean isActive;
    @JsonProperty("email")
    private String email;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("tenancyName")
    public String getTenancyName() {
        return tenancyName;
    }

    @JsonProperty("tenancyName")
    public void setTenancyName(String tenancyName) {
        this.tenancyName = tenancyName;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("adminEmailAddress")
    public String getAdminEmailAddress() {
        return adminEmailAddress;
    }

    @JsonProperty("adminEmailAddress")
    public void setAdminEmailAddress(String adminEmailAddress) {
        this.adminEmailAddress = adminEmailAddress;
    }

    @JsonProperty("connectionString")
    public String getConnectionString() {
        return connectionString;
    }

    @JsonProperty("connectionString")
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    @JsonProperty("isActive")
    public Boolean getIsActive() {
        return isActive;
    }

    @JsonProperty("isActive")
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
