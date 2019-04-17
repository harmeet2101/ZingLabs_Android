
package com.zing.model.response.PreviousPaystubResponse;

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
    @SerializedName("upcoming_payment")
    @Expose
    private String upcomingPayment;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("previous_paystubs")
    @Expose
    private List<PreviousPaystub> previousPaystubs = null;

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

    public String getUpcomingPayment() {
        return upcomingPayment;
    }

    public void setUpcomingPayment(String upcomingPayment) {
        this.upcomingPayment = upcomingPayment;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public List<PreviousPaystub> getPreviousPaystubs() {
        return previousPaystubs;
    }

    public void setPreviousPaystubs(List<PreviousPaystub> previousPaystubs) {
        this.previousPaystubs = previousPaystubs;
    }

}
