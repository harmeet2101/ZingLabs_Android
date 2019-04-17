
package com.zing.model.response.LoginResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("response")
    @Expose
    private LResponse response;

    public LResponse getResponse() {
        return response;
    }

    public void setResponse(LResponse response) {
        this.response = response;
    }

}
