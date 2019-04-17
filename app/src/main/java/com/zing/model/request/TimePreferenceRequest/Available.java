
package com.zing.model.request.TimePreferenceRequest;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Available {

    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("time_slot")
    @Expose
    private ArrayList<String> timeSlot = null;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<String> getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(ArrayList<String> timeSlot) {
        this.timeSlot = timeSlot;
    }

}
