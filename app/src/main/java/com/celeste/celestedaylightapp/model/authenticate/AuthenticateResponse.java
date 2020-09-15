package com.celeste.celestedaylightapp.model.authenticate;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonPropertyOrder({
        "accessToken",
        "encryptedAccessToken",
        "expireInSeconds",
        "userId"
})
public class AuthenticateResponse {
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("encryptedAccessToken")
    private String encryptedAccessToken;
    @JsonProperty("expireInSeconds")
    private Integer expireInSeconds;
    @JsonProperty("userId")
    private Integer userId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("accessToken")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("accessToken")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("encryptedAccessToken")
    public String getEncryptedAccessToken() {
        return encryptedAccessToken;
    }

    @JsonProperty("encryptedAccessToken")
    public void setEncryptedAccessToken(String encryptedAccessToken) {
        this.encryptedAccessToken = encryptedAccessToken;
    }

    @JsonProperty("expireInSeconds")
    public Integer getExpireInSeconds() {
        return expireInSeconds;
    }

    @JsonProperty("expireInSeconds")
    public void setExpireInSeconds(Integer expireInSeconds) {
        this.expireInSeconds = expireInSeconds;
    }

    @JsonProperty("userId")
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(Integer userId) {
        this.userId = userId;
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
