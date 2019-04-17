package com.zing.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShiftBreak {

    @SerializedName("break")
    @Expose
    private String breakValue;
    @SerializedName("shift_id")
    @Expose
    private String shiftId;


    public ShiftBreak(String breakValue, String shiftId) {
        this.breakValue = breakValue;
        this.shiftId = shiftId;
    }

    public String getBreakValue() {
        return breakValue;
    }

    public void setBreakValue(String breakValue) {
        this.breakValue = breakValue;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }
}
