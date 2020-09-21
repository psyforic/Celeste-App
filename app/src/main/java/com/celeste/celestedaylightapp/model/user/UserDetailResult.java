package com.celeste.celestedaylightapp.model.user;

import com.celeste.celestedaylightapp.model.usermode.UserMode;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userName",
        "name",
        "surname",
        "emailAddress",
        "isActive",
        "fullName",
        "lastLoginTime",
        "creationTime",
        "roleNames",
        "address",
        "city",
        "province",
        "suburb",
        "postalCode",
        "cellphoneNumber",
        "userModes",
        "id"
})
public class UserDetailResult {
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("name")
    private String name;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("emailAddress")
    private String emailAddress;
    @JsonProperty("isActive")
    private Boolean isActive;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("lastLoginTime")
    private String lastLoginTime;
    @JsonProperty("creationTime")
    private String creationTime;
    @JsonProperty("roleNames")
    private List<String> roleNames = null;
    @JsonProperty("address")
    private String address;
    @JsonProperty("city")
    private String city;
    @JsonProperty("province")
    private String province;
    @JsonProperty("suburb")
    private String suburb;
    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty("cellphoneNumber")
    private String cellphoneNumber;
    @JsonProperty("userModes")
    private List<UserMode> userModes = null;
    @JsonProperty("id")
    private Integer id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("userName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("surname")
    public String getSurname() {
        return surname;
    }

    @JsonProperty("surname")
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonProperty("emailAddress")
    public String getEmailAddress() {
        return emailAddress;
    }

    @JsonProperty("emailAddress")
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @JsonProperty("isActive")
    public Boolean getIsActive() {
        return isActive;
    }

    @JsonProperty("isActive")
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @JsonProperty("fullName")
    public String getFullName() {
        return fullName;
    }

    @JsonProperty("fullName")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonProperty("lastLoginTime")
    public Object getLastLoginTime() {
        return lastLoginTime;
    }

    @JsonProperty("lastLoginTime")
    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @JsonProperty("creationTime")
    public String getCreationTime() {
        return creationTime;
    }

    @JsonProperty("creationTime")
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    @JsonProperty("roleNames")
    public List<String> getRoleNames() {
        return roleNames;
    }

    @JsonProperty("roleNames")
    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("province")
    public String getProvince() {
        return province;
    }

    @JsonProperty("province")
    public void setProvince(String province) {
        this.province = province;
    }

    @JsonProperty("suburb")
    public String getSuburb() {
        return suburb;
    }

    @JsonProperty("suburb")
    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    @JsonProperty("postalCode")
    public String getPostalCode() {
        return postalCode;
    }

    @JsonProperty("postalCode")
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @JsonProperty("cellphoneNumber")
    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    @JsonProperty("cellphoneNumber")
    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    @JsonProperty("userModes")
    public List<UserMode> getUserModes() {
        return userModes;
    }

    @JsonProperty("userModes")
    public void setUserModes(List<UserMode> userModes) {
        this.userModes = userModes;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
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
