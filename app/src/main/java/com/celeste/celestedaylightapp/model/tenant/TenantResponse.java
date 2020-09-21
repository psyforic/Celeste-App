package com.celeste.celestedaylightapp.model.tenant;

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
        "result",
        "targetUrl",
        "success",
        "error",
        "unAuthorizedRequest",
        "__abp"
})
public class TenantResponse {
    @JsonProperty("result")
    private TenantLoginResult result;
    @JsonProperty("targetUrl")
    private String targetUrl;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("error")
    private Object error;
    @JsonProperty("unAuthorizedRequest")
    private Boolean unAuthorizedRequest;
    @JsonProperty("__abp")
    private Boolean abp;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("result")
    public TenantLoginResult getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(TenantLoginResult result) {
        this.result = result;
    }

    @JsonProperty("targetUrl")
    public Object getTargetUrl() {
        return targetUrl;
    }

    @JsonProperty("targetUrl")
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @JsonProperty("success")
    public Boolean getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @JsonProperty("error")
    public Object getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Object error) {
        this.error = error;
    }

    @JsonProperty("unAuthorizedRequest")
    public Boolean getUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    @JsonProperty("unAuthorizedRequest")
    public void setUnAuthorizedRequest(Boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }

    @JsonProperty("__abp")
    public Boolean getAbp() {
        return abp;
    }

    @JsonProperty("__abp")
    public void setAbp(Boolean abp) {
        this.abp = abp;
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
