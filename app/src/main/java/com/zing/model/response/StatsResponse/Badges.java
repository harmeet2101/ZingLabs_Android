package com.zing.model.response.StatsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Badges {

    @SerializedName("first_shift_completed")
    @Expose
    Integer first_shift_completed;
    @SerializedName("show_up_on_time")
    @Expose
    Integer show_up_on_time;
    @SerializedName("perfect_week")
    @Expose
    Integer perfect_week;
    @SerializedName("perfect_month")
    @Expose
    Integer perfect_month;
    @SerializedName("recommended_shift_picked")
    @Expose
    Integer recommended_shift_picked;


    public Integer getFirst_shift_completed() {
        return first_shift_completed;
    }

    public void setFirst_shift_completed(Integer first_shift_completed) {
        this.first_shift_completed = first_shift_completed;
    }

    public Integer getShow_up_on_time() {
        return show_up_on_time;
    }

    public void setShow_up_on_time(Integer show_up_on_time) {
        this.show_up_on_time = show_up_on_time;
    }

    public Integer getPerfect_week() {
        return perfect_week;
    }

    public void setPerfect_week(Integer perfect_week) {
        this.perfect_week = perfect_week;
    }

    public Integer getPerfect_month() {
        return perfect_month;
    }

    public void setPerfect_month(Integer perfect_month) {
        this.perfect_month = perfect_month;
    }

    public Integer getRecommended_shift_picked() {
        return recommended_shift_picked;
    }

    public void setRecommended_shift_picked(Integer recommended_shift_picked) {
        this.recommended_shift_picked = recommended_shift_picked;
    }


    @Override
    public String toString() {
        return "Badges{" +
                "first_shift_completed=" + first_shift_completed +
                ", show_up_on_time=" + show_up_on_time +
                ", perfect_week=" + perfect_week +
                ", perfect_month=" + perfect_month +
                ", recommended_shift_picked=" + recommended_shift_picked +
                '}';
    }
}
