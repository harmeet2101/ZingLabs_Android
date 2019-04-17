
package com.zing.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Week {

    @SerializedName("earnings(y-axis)")
    @Expose
    private String earningsYAxis;
    @SerializedName("day(x-axis)")
    @Expose
    private String dayXAxis;
    @SerializedName("day")
    @Expose
    private float day;
    @SerializedName("shift")
    @Expose
    private String shift;

    public String getEarningsYAxis() {
        return earningsYAxis;
    }

    public void setEarningsYAxis(String earningsYAxis) {
        this.earningsYAxis = earningsYAxis;
    }

    public String getDayXAxis() {
        return dayXAxis;
    }

    public void setDayXAxis(String dayXAxis) {
        this.dayXAxis = dayXAxis;
    }

    public float getDay() {
        return day;
    }

    public void setDay(float day) {
        this.day = day;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

}
