
package com.zing.model.response.CalendarMonthResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalendarMonthResponse {

    @SerializedName("response")
    @Expose
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}
