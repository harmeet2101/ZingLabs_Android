
package com.zing.model.response.GetTimePreferenceResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlot_ {

    @SerializedName("time_slot")
    @Expose
    private List<String> timeSlot = null;

    public List<String> getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(List<String> timeSlot) {
        this.timeSlot = timeSlot;
    }

}
