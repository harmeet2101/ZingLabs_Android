package com.zing.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishek on 28/6/18.
 */

public class HomeRequest {
    @SerializedName("today_date")
    @Expose
    private String todayDate;
    @SerializedName("timezone")
    @Expose
    private String timezone;

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

}