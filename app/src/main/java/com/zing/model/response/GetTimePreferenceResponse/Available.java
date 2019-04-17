
package com.zing.model.response.GetTimePreferenceResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Available {

    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("time_slot")
    @Expose
    private TimeSlot timeSlot;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

}
