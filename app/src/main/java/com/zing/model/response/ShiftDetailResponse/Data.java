
package com.zing.model.response.ShiftDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("time_slot")
    @Expose
    private String timeSlot;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("expected_earning")
    @Expose
    private String expectedEarning;
    @SerializedName("shift_id")
    @Expose
    private String shiftId;
    @SerializedName("location")
    @Expose
    private String location;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getExpectedEarning() {
        return expectedEarning;
    }

    public void setExpectedEarning(String expectedEarning) {
        this.expectedEarning = expectedEarning;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
