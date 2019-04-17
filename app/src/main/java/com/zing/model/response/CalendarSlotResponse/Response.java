
package com.zing.model.response.CalendarSlotResponse;

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
    @SerializedName("slot")
    @Expose
    private List<Slot> slot = null;
    @SerializedName("recommended_shifts")
    @Expose
    private List<RecommendedShift> recommendedShifts = null;

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

    public List<Slot> getSlot() {
        return slot;
    }

    public void setSlot(List<Slot> slot) {
        this.slot = slot;
    }

    public List<RecommendedShift> getRecommendedShifts() {
        return recommendedShifts;
    }

    public void setRecommendedShifts(List<RecommendedShift> recommendedShifts) {
        this.recommendedShifts = recommendedShifts;
    }

}