
package com.zing.model.response.PreviousPaystubResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreviousPaystub {

    @SerializedName("earnings")
    @Expose
    private String earnings;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("status")
    @Expose
    private String status;

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    @SerializedName("slot")
    @Expose

    private String slot;

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
