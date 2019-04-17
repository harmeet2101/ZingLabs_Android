
package com.zing.model.response.BroadcastResponse;

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
    @SerializedName("broadcast_list")
    @Expose
    private List<BroadcastList> broadcastList = null;

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

    public List<BroadcastList> getBroadcastList() {
        return broadcastList;
    }

    public void setBroadcastList(List<BroadcastList> broadcastList) {
        this.broadcastList = broadcastList;
    }

}
