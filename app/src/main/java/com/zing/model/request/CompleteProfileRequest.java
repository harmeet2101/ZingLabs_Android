
package com.zing.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompleteProfileRequest {

    @SerializedName("profile_image")
    @Expose
    private String profileImage;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @SerializedName("timezone")
    @Expose
    private String timeZone;

    public String getIs_new_image() {
        return is_new_image;
    }

    public void setIs_new_image(String is_new_image) {
        this.is_new_image = is_new_image;
    }

    @SerializedName("is_new_image")
    @Expose

    private String is_new_image;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("data_updated")
    @Expose
    private String dataUpdated;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("street_address")
    @Expose
    private String streetAddress;
    @SerializedName("apt")
    @Expose
    private String apt;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("zip_code")
    @Expose
    private String zipCode;
    @SerializedName("ssn")
    @Expose
    private String ssn;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @SerializedName("device_token")
    @Expose
    private String deviceToken;


    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @SerializedName("country_id")
    @Expose
    private String countryId;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getDataUpdated() {
        return dataUpdated;
    }

    public void setDataUpdated(String dataUpdated) {
        this.dataUpdated = dataUpdated;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getApt() {
        return apt;
    }

    public void setApt(String apt) {
        this.apt = apt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

}
