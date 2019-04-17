
package com.zing.model.response.CalendarMonthResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("calendar")
    @Expose
    private List<CalendarMonth> calendar = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CalendarMonth> getCalendar() {
        return calendar;
    }

    public void setCalendar(List<CalendarMonth> calendar) {
        this.calendar = calendar;
    }

}
