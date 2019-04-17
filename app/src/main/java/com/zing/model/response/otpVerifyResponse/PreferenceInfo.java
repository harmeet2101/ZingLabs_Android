package com.zing.model.response.otpVerifyResponse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PreferenceInfo implements Serializable {


    @SerializedName("available")
    private List<DayAndTime> availableSlots;
    @SerializedName("preferred")
    private List<DayAndTime> preferredSlots;


    public List<DayAndTime> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<DayAndTime> availableSlots) {
        this.availableSlots = availableSlots;
    }

    public List<DayAndTime> getPreferredSlots() {
        return preferredSlots;
    }

    public void setPreferredSlots(List<DayAndTime> preferredSlots) {
        this.preferredSlots = preferredSlots;
    }
}
