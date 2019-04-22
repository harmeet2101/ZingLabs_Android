
package com.zing.model.response.shiftbydate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShiftByDateBaseModel {

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
