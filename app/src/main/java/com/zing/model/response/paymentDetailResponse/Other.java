
package com.zing.model.response.paymentDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Other {

    @SerializedName("over_time")
    @Expose
    private String overTime;

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

}
