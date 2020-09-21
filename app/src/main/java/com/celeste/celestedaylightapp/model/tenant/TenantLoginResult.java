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
        "state",
        "tenantId"
})
public class TenantLoginResult {

    @JsonProperty("state")
    private TenantAvailabilityState state;
    @JsonProperty("tenantId")
    private Integer tenantId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("state")
    public TenantAvailabilityState getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(TenantAvailabilityState state) {
        this.state = state;
    }

    @JsonProperty("tenantId")
    public Integer getTenantId() {
        return tenantId;
    }

    @JsonProperty("tenantId")
    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
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