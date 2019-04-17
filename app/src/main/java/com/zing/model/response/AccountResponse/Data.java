
package com.zing.model.response.AccountResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("personal_info")
    @Expose
    private PersonalInfo personalInfo;
    @SerializedName("payment_details")
    @Expose
    private PaymentDetails paymentDetails;
    @SerializedName("time_off")
    @Expose
    private List<TimeOff> timeOff = null;

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public List<TimeOff> getTimeOff() {
        return timeOff;
    }

    public void setTimeOff(List<TimeOff> timeOff) {
        this.timeOff = timeOff;
    }

}
