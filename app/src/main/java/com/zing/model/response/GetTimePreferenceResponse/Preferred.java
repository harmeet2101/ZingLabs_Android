
package com.zing.model.response.GetTimePreferenceResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Preferred {

    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("time_slot")
    @Expose
    private TimeSlot_ timeSlot;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public TimeSlot_ getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot_ timeSlot) {
        this.timeSlot = timeSlot;
    }

}
