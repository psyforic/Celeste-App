package com.celeste.celestedaylightapp.model.authenticate;

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
        "userNameOrEmailAddress",
        "password",
        "rememberClient"
})
public class AuthenticateModel {
    @JsonProperty("userNameOrEmailAddress")
    private String userNameOrEmailAddress;
    @JsonProperty("password")
    private String password;
    @JsonProperty("rememberClient")
    private Boolean rememberClient;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public AuthenticateModel(String userNameOrEmailAddress, String password) {
        this.userNameOrEmailAddress = userNameOrEmailAddress;
        this.password = password;
    }

    @JsonProperty("userNameOrEmailAddress")
    public String getUserNameOrEmailAddress() {
        return userNameOrEmailAddress;
    }

    @JsonProperty("userNameOrEmailAddress")
    public void setUserNameOrEmailAddress(String userNameOrEmailAddress) {
        this.userNameOrEmailAddress = userNameOrEmailAddress;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("rememberClient")
    public Boolean getRememberClient() {
        return rememberClient;
    }

    @JsonProperty("rememberClient")
    public void setRememberClient(Boolean rememberClient) {
        this.rememberClient = rememberClient;
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
