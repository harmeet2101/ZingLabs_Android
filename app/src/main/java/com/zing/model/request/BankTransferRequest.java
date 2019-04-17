
package com.zing.model.request;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankTransferRequest implements Parcelable
{

    @SerializedName("amount")
    @Expose
    private String amount;
    public final static Creator<BankTransferRequest> CREATOR = new Creator<BankTransferRequest>() {


        @SuppressWarnings({
            "unchecked"
        })
        public BankTransferRequest createFromParcel(Parcel in) {
            return new BankTransferRequest(in);
        }

        public BankTransferRequest[] newArray(int size) {
            return (new BankTransferRequest[size]);
        }

    }
    ;

    protected BankTransferRequest(Parcel in) {
        this.amount = ((String) in.readValue((String.class.getClassLoader())));
    }

    public BankTransferRequest() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(amount);
    }

    public int describeContents() {
        return  0;
    }

}
