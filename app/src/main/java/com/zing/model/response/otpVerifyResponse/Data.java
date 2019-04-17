package com.zing.model.response.otpVerifyResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("basic_info")
    @Expose
    private BasicInfo basicInfo;

    /*public PreferenceInfo getPreferenceInfo() {
        return preferenceInfo;
    }

    public void setPreferenceInfo(PreferenceInfo preferenceInfo) {
        this.preferenceInfo = preferenceInfo;
    }

    @SerializedName("preference_info")
    @Expose
    private PreferenceInfo preferenceInfo = null;*/

    public BasicInfo getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }





}
