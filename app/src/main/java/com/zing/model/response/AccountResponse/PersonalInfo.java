
package com.zing.model.response.AccountResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalInfo {

    @SerializedName("DOB")
    @Expose
    private String dOB;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @SerializedName("state")
    @Expose

    private String state;
    @SerializedName("zip_code")
    @Expose
    private String zip_code;

    public String getDOB() {
        return dOB;
    }

    public void setDOB(String dOB) {
        this.dOB = dOB;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
