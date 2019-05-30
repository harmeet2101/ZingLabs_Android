
package com.zing.model.response.shiftbydate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shift {

    @SerializedName("shift_id")
    @Expose
    private String shiftId;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time_slot")
    @Expose
    private String timeSlot;
    @SerializedName("expected_earning")
    @Expose
    private String expectedEarning;
    @SerializedName("earning_amount")
    @Expose
    private String earningAmount;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("total_break_time")
    @Expose
    private String totalBreakTime;
    @SerializedName("last_break_in_date")
    @Expose
    private String lastBreakInDate;
    @SerializedName("last_break_in_time")
    @Expose
    private String lastBreakInTime;
    @SerializedName("checkin_time")
    @Expose
    private String checkinTime;
    @SerializedName("checkout_time")
    @Expose
    private String checkoutTime;
    @SerializedName("is_on_break")
    @Expose
    private Integer isOnBreak;
    @SerializedName("shift_status")
    @Expose
    private String shiftStatus;
    @SerializedName("manager_number")
    @Expose
    private String managerNumber;
    @SerializedName("release")
    @Expose
    private Integer release;

    public Boolean getAuto_checkin() {
        return auto_checkin;
    }

    public void setAuto_checkin(Boolean auto_checkin) {
        this.auto_checkin = auto_checkin;
    }

    @SerializedName("auto_checkin")
    @Expose
    private Boolean auto_checkin;


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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public String getEarningAmount() {
        return earningAmount;
    }

    public void setEarningAmount(String earningAmount) {
        this.earningAmount = earningAmount;
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

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
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

    public String getManagerNumber() {
        return managerNumber;
    }

    public void setManagerNumber(String managerNumber) {
        this.managerNumber = managerNumber;
    }

    public Integer getRelease() {
        return release;
    }

    public void setRelease(Integer release) {
        this.release = release;
    }

}
