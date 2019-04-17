package com.zing.model.response.breakShift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    @SerializedName("total_break_time")
    @Expose
    private String total_break_time;
    @SerializedName("last_break_in_date")
    @Expose
    private String last_break_in_date;
    @SerializedName("last_break_in_time")
    @Expose
    private String last_break_in_time;



    public String getTotal_break_time() {
        return total_break_time;
    }

    public void setTotal_break_time(String total_break_time) {
        this.total_break_time = total_break_time;
    }

    public String getLast_break_in_date() {
        return last_break_in_date;
    }

    public void setLast_break_in_date(String last_break_in_date) {
        this.last_break_in_date = last_break_in_date;
    }

    public String getLast_break_in_time() {
        return last_break_in_time;
    }

    public void setLast_break_in_time(String last_break_in_time) {
        this.last_break_in_time = last_break_in_time;
    }
}
