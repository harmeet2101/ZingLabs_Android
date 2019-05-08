
package com.zing.model.response.StatsResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;

    public RateInfo getShow_rate() {
        return show_rate;
    }

    public void setShow_rate(RateInfo show_rate) {
        this.show_rate = show_rate;
    }

    @SerializedName("show_rate")
    @Expose
    private RateInfo show_rate;
    @SerializedName("badges")
    @Expose
    private Badges badges = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public Badges getBadges() {
        return badges;
    }

    public void setBadges(Badges badges) {
        this.badges = badges;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", rate='" + show_rate + '\'' +
                ", badges=" + badges +
                '}';
    }
}
