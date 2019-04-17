
package com.zing.model.response.paymentDetailResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("upcoming_payment")
    @Expose
    private String upcomingPayment;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("total_wages")
    @Expose
    private String totalWages;
    @SerializedName("tips")
    @Expose
    private String tips;
    @SerializedName("other")
    @Expose
    private List<Other> other = null;
    @SerializedName("bank_details")
    @Expose
    private List<BankDetail> bankDetails = null;
    @SerializedName("included_shifts")
    @Expose
    private List<IncludedShift> includedShifts = null;

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

    public String getTotalWages() {
        return totalWages;
    }

    public void setTotalWages(String totalWages) {
        this.totalWages = totalWages;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public List<Other> getOther() {
        return other;
    }

    public void setOther(List<Other> other) {
        this.other = other;
    }

    public List<BankDetail> getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(List<BankDetail> bankDetails) {
        this.bankDetails = bankDetails;
    }

    public List<IncludedShift> getIncludedShifts() {
        return includedShifts;
    }

    public void setIncludedShifts(List<IncludedShift> includedShifts) {
        this.includedShifts = includedShifts;
    }

}
