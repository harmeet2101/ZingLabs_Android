
package com.zing.model.response.HomeResponse;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpcomingShift implements Serializable {

    @SerializedName("shift_id")
    @Expose
    private String shiftId;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time_slot")
    @Expose
    private String timeSlot;
    @SerializedName("expected_earning")
    @Expose
    private String expectedEarning;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("release")
    @Expose
    private String release;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("earning_amount")
    private String earningAmount;
    @SerializedName("total_break_time")
    private String totalBreakTime;
    @SerializedName("last_break_in_date")
    private String lastBreakInDate;
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getEarningAmount() {
        return earningAmount;
    }

    public void setEarningAmount(String earningAmount) {
        this.earningAmount = earningAmount;
    }

    public String getTotalBreakTime() {
        return totalBreakTime;
    }

    public void setTotalBreakTime(String totalBreakTime) {
        this.totalBreakTime = totalBreakTime;
    }

    public String getLastBreakInDate() {
        return lastBreakInDate;
    }

    public void setLastBreakInDate(String lastBreakInDate) {
        this.lastBreakInDate = lastBreakInDate;
    }

    public String getLastBreakInTime() {
        return lastBreakInTime;
    }

    public void setLastBreakInTime(String lastBreakInTime) {
        this.lastBreakInTime = lastBreakInTime;
    }

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

    public Integer isOnBreak() {
        return isOnBreak;
    }

    public void setOnBreak(Integer onBreak) {
        isOnBreak = onBreak;
    }

    public String getShiftStatus() {
        return shiftStatus;
    }

    public void setShiftStatus(String shiftStatus) {
        this.shiftStatus = shiftStatus;
    }

    @SerializedName("last_break_in_time")
    private String lastBreakInTime;

    @SerializedName("checkin_time")
    private String checkInTime;
    @SerializedName("checkout_time")
    private String checkOutTime;
    @SerializedName("is_on_break")
    private Integer isOnBreak;
    @SerializedName("shift_status")
    private String shiftStatus;


    public Long getShift_start_timestamp() {
        return shift_start_timestamp;
    }

    public void setShift_start_timestamp(Long shift_start_timestamp) {
        this.shift_start_timestamp = shift_start_timestamp;
    }

    @SerializedName("shift_start_timestamp")
    private Long shift_start_timestamp;
    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }


    protected UpcomingShift(Parcel in) {
        this.shiftId = ((String) in.readValue((String.class.getClassLoader())));
        this.day = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.timeSlot = ((String) in.readValue((String.class.getClassLoader())));
        this.expectedEarning = ((String) in.readValue((String.class.getClassLoader())));
        this.role = ((String) in.readValue((String.class.getClassLoader())));
        this.location = ((String) in.readValue((String.class.getClassLoader())));
        this.release = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UpcomingShift() {
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getExpectedEarning() {
        return expectedEarning;
    }

    public void setExpectedEarning(String expectedEarning) {
        this.expectedEarning = expectedEarning;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
