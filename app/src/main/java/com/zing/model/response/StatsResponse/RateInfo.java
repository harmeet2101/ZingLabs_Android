package com.zing.model.response.StatsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RateInfo {

    @SerializedName("label")
    @Expose
    String label;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("rate")
    @Expose
    Integer rate;


    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public Integer getRate() {
        return rate;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }


    @Override
    public String toString() {
        return "RateInfo{" +
                "label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", rate=" + rate +
                '}';
    }
}
