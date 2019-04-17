package com.zing.model.response.otpVerifyResponse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DayAndTime implements Serializable {

    @SerializedName("day")
    private String day;
    @SerializedName("time_slot")
    private List<String> timeSlots;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<String> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<String> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
