
package com.zing.model.response.GetBusinessHourResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetBusinessHours {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<GetBusinessHoursData> data = null;

    @SerializedName("code")
    @Expose
    private Integer code;




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<GetBusinessHoursData> getData() {
        return data;
    }

    public void setData(List<GetBusinessHoursData> data) {
        this.data = data;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }








}
