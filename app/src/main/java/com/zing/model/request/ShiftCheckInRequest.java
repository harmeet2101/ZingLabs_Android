
package com.zing.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShiftCheckInRequest {

    @SerializedName("shift_id")
    @Expose
    private String shiftId;

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

}
