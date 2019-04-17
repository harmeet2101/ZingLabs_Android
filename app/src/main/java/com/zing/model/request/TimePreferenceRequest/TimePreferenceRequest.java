
package com.zing.model.request.TimePreferenceRequest;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimePreferenceRequest {

    @SerializedName("available")
    @Expose
    private ArrayList<Available> available = null;
    @SerializedName("preferred")
    @Expose
    private ArrayList<Preffered> preffered = null;

    public ArrayList<Available> getAvailable() {
        return available;
    }

    public void setAvailable(ArrayList<Available> available) {
        this.available = available;
    }

    public ArrayList<Preffered> getPreffered() {
        return preffered;
    }

    public void setPreffered(ArrayList<Preffered> preffered) {
        this.preffered = preffered;
    }

}
