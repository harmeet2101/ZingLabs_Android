
package com.zing.model.response.HomeResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Serializable {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("next_shift")
    @Expose
    private String nextShift;
    @SerializedName("expected_earning")
    @Expose
    private String expectedEarning;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("shift_id")
    @Expose
    private String shiftId;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("total_earnings")
    @Expose
    private String totalEarnings;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("manager_number")
    @Expose
    private String manager_number;
    @SerializedName("check_in_status")
    @Expose
    private String check_in_status;
    @SerializedName("starts_at")
    @Expose
    private String starts_at;
    @SerializedName("registration_date")
    @Expose
    private String registration_date;
    @SerializedName("time_preferences")
    @Expose
    private String time_preferences;

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Integer getIsOnBreak() {
        return isOnBreak;
    }

    public void setIsOnBreak(Integer isOnBreak) {
        this.isOnBreak = isOnBreak;
    }

    public String getShiftStatus() {
        return shiftStatus;
    }

    public void setShiftStatus(String shiftStatus) {
        this.shiftStatus = shiftStatus;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @SerializedName("checkin_time")
    private String checkInTime;
    @SerializedName("checkout_time")
    private String checkOutTime;
    @SerializedName("is_on_break")
    private Integer isOnBreak;
    @SerializedName("shift_status")
    private String shiftStatus;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("upcoming_shifts")
    @Expose
    private ArrayList<UpcomingShift> upcomingShifts = null;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Response() {

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNextShift() {
        return nextShift;
    }

    public void setNextShift(String nextShift) {
        this.nextShift = nextShift;
    }

    public String getExpectedEarning() {
        return expectedEarning;
    }

    public void setExpectedEarning(String expectedEarning) {
        this.expectedEarning = expectedEarning;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(String totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<UpcomingShift> getUpcomingShifts() {
        return upcomingShifts;
    }

    public void setUpcomingShifts(ArrayList<UpcomingShift> upcomingShifts) {
        this.upcomingShifts = upcomingShifts;
    }

    public String getManager_number() {
        return manager_number;
    }

    public void setManager_number(String manager_number) {
        this.manager_number = manager_number;
    }

    public String getCheck_in_status() {
        return check_in_status;
    }

    public void setCheck_in_status(String check_in_status) {
        this.check_in_status = check_in_status;
    }

    public String getStarts_at() {
        return starts_at;
    }

    public void setStarts_at(String starts_at) {
        this.starts_at = starts_at;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getTime_preferences() {
        return time_preferences;
    }

    public void setTime_preferences(String time_preferences) {
        this.time_preferences = time_preferences;
    }

}
