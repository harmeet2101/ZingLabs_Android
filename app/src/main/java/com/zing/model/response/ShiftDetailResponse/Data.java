
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

    public String getEarning_amount() {
        return earning_amount;
    }

    public void setEarning_amount(String earning_amount) {
        this.earning_amount = earning_amount;
    }

    @SerializedName("earning_amount")
    @Expose
    private String earning_amount;

    @SerializedName("shift_id")
    @Expose
    private String shiftId;
    @SerializedName("location")
    @Expose
    private String location;

    public Integer getRelease() {
        return release;
    }

    public void setRelease(Integer release) {
        this.release = release;
    }

    @SerializedName("release")
    @Expose
    private Integer release;

    public Integer getIs_on_break() {
        return is_on_break;
    }

    public void setIs_on_break(Integer is_on_break) {
        this.is_on_break = is_on_break;
    }

    @SerializedName("is_on_break")
    @Expose
    private  Integer is_on_break;


    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    @SerializedName("store_name")
    @Expose
    private String store_name;

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


    public String getTotal_break_time() {
        return total_break_time;
    }

    public void setTotal_break_time(String total_break_time) {
        this.total_break_time = total_break_time;
    }

    @SerializedName("total_break_time")
    @Expose
    private String total_break_time;
    @SerializedName("last_break_in_time")
    @Expose
    private  String last_break_in_time;

    @SerializedName("checkin_time")
    @Expose
    private String checkin_time;
    @SerializedName("checkout_time")
    @Expose
    private String checkout_time;
    @SerializedName("shift_status")
    @Expose
    private String shift_status;
    @SerializedName("manager_number")
    @Expose
    private  String manager_number;

    public String getLast_break_in_time() {
        return last_break_in_time;
    }

    public void setLast_break_in_time(String last_break_in_time) {
        this.last_break_in_time = last_break_in_time;
    }

    public String getCheckin_time() {
        return checkin_time;
    }

    public void setCheckin_time(String checkin_time) {
        this.checkin_time = checkin_time;
    }

    public String getCheckout_time() {
        return checkout_time;
    }

    public void setCheckout_time(String checkout_time) {
        this.checkout_time = checkout_time;
    }

    public String getShift_status() {
        return shift_status;
    }

    public void setShift_status(String shift_status) {
        this.shift_status = shift_status;
    }

    public String getManager_number() {
        return manager_number;
    }

    public void setManager_number(String manager_number) {
        this.manager_number = manager_number;
    }
}
