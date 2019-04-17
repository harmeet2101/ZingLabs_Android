
package com.zing.model.response.CalendarMonthResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalendarMonth {

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("shift_id")
    @Expose
    private String shiftId;

    public String getEvent_color() {
        return event_color;
    }

    public void setEvent_color(String event_color) {
        this.event_color = event_color;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    @SerializedName("event_color")

    @Expose

    private String event_color;
    @SerializedName("event")
    @Expose
    private String event;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

}
