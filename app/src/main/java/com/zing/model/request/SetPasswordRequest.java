package com.zing.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishek on 7/6/18.
 */

public class SetPasswordRequest {
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SerializedName("phone")
    @Expose

    private String phone;

    @SerializedName("password")
    @Expose
    private String password;
}
