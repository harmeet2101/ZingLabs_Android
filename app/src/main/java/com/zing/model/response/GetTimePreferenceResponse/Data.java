
package com.zing.model.response.GetTimePreferenceResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zing.model.request.TimePreferenceRequest.Available;
import com.zing.model.request.TimePreferenceRequest.Preffered;

public class Data {

    @SerializedName("available")
    @Expose
    private List<Available> available = null;
    @SerializedName("preferred")
    @Expose
    private List<Preffered> preferred = null;

    public List<Available> getAvailable() {
        return available;
    }

    public void setAvailable(List<Available> available) {
        this.available = available;
    }

    public List<Preffered> getPreferred() {
        return preferred;
    }

    public void setPreferred(List<Preffered> preferred) {
        this.preferred = preferred;
    }

}
