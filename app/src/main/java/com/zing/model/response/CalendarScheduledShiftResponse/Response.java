
package com.zing.model.response.CalendarScheduledShiftResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zing.model.response.CalendarSlotResponse.RecommendedShift;
import com.zing.model.response.HomeResponse.UpcomingShift;

public class Response implements Serializable
{

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("scheduled_shifts")
    @Expose
    private List<UpcomingShift> scheduledShifts = null;
    @SerializedName("recommended_shifts")
    @Expose
    private ArrayList<RecommendedShift> recommendedShiftList =null;

    @SerializedName("completed_shifts")
    @Expose
    private ArrayList<RecommendedShift> completedShiftList=null;


    public Long getShift_start_timestamp() {
        return shift_start_timestamp;
    }

    public void setShift_start_timestamp(Long shift_start_timestamp) {
        this.shift_start_timestamp = shift_start_timestamp;
    }

    @SerializedName("shift_start_timestamp")
    private Long shift_start_timestamp;

    public ArrayList<RecommendedShift> getRecommendedShiftList() {
        return recommendedShiftList;
    }

    public void setRecommendedShiftList(ArrayList<RecommendedShift> recommendedShiftList) {
        this.recommendedShiftList = recommendedShiftList;
    }

    public ArrayList<RecommendedShift> getCompletedShiftList() {
        return completedShiftList;
    }

    public void setCompletedShiftList(ArrayList<RecommendedShift> completedShiftList) {
        this.completedShiftList = completedShiftList;
    }

    public Response() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UpcomingShift> getScheduledShifts() {
        return scheduledShifts;
    }

    public void setScheduledShifts(List<UpcomingShift> scheduledShifts) {
        this.scheduledShifts = scheduledShifts;
    }

}
