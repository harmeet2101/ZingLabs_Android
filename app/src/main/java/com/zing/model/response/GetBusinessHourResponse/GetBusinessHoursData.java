
package com.zing.model.response.GetBusinessHourResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetBusinessHoursData {

    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("range")
    @Expose
    private List<Double> range = null;
    @SerializedName("start_time")
    @Expose
    private List<String> startTime = null;
    @SerializedName("end_time")
    @Expose
    private List<String> endTime = null;
    @SerializedName("isSelected")
    @Expose
    private Boolean isSelected;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<Double> getRange() {
        return range;
    }

    public void setRange(List<Double> range) {
        this.range = range;
    }

    public List<String> getStartTime() {
        return startTime;
    }

    public void setStartTime(List<String> startTime) {
        this.startTime = startTime;
    }

    public List<String> getEndTime() {
        return endTime;
    }

    public void setEndTime(List<String> endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

}
