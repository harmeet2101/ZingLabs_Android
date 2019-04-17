
package com.zing.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyCheckInRequest {

    @SerializedName("pin")
    @Expose
    private String pin;
    @SerializedName("shift_id")
    @Expose
    private String shiftId;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

}
