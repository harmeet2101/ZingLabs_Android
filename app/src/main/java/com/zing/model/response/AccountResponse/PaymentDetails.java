
package com.zing.model.response.AccountResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDetails {

    @SerializedName("account_no")
    @Expose
    private String accountNo;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("bank_id")
    @Expose
    private String bankId;
    @SerializedName("routing_no")
    @Expose
    private String routingNo;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getRoutingNo() {
        return routingNo;
    }

    public void setRoutingNo(String routingNo) {
        this.routingNo = routingNo;
    }

}
