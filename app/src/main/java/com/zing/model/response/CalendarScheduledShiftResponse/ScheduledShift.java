//
//package com.zing.model.response.CalendarScheduledShiftResponse;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//import android.os.Parcelable.Creator;
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//public class ScheduledShift implements Parcelable
//{
//
//    @SerializedName("shift_id")
//    @Expose
//    private String shiftId;
//    @SerializedName("day")
//    @Expose
//    private String day;
//    @SerializedName("date")
//    @Expose
//    private String date;
//    @SerializedName("time_slot")
//    @Expose
//    private String timeSlot;
//    @SerializedName("expected_earning")
//    @Expose
//    private String expectedEarning;
//    @SerializedName("role")
//    @Expose
//    private String role;
//    @SerializedName("location")
//    @Expose
//    private String location;
//    public final static Creator<ScheduledShift> CREATOR = new Creator<ScheduledShift>() {
//
//
//        @SuppressWarnings({
//            "unchecked"
//        })
//        public ScheduledShift createFromParcel(Parcel in) {
//            return new ScheduledShift(in);
//        }
//
//        public ScheduledShift[] newArray(int size) {
//            return (new ScheduledShift[size]);
//        }
//
//    }
//    ;
//
//    protected ScheduledShift(Parcel in) {
//        this.shiftId = ((String) in.readValue((String.class.getClassLoader())));
//        this.day = ((String) in.readValue((String.class.getClassLoader())));
//        this.date = ((String) in.readValue((String.class.getClassLoader())));
//        this.timeSlot = ((String) in.readValue((String.class.getClassLoader())));
//        this.expectedEarning = ((String) in.readValue((String.class.getClassLoader())));
//        this.role = ((String) in.readValue((String.class.getClassLoader())));
//        this.location = ((String) in.readValue((String.class.getClassLoader())));
//    }
//
//    public ScheduledShift() {
//    }
//
//    public String getShiftId() {
//        return shiftId;
//    }
//
//    public void setShiftId(String shiftId) {
//        this.shiftId = shiftId;
//    }
//
//    public String getDay() {
//        return day;
//    }
//
//    public void setDay(String day) {
//        this.day = day;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public String getTimeSlot() {
//        return timeSlot;
//    }
//
//    public void setTimeSlot(String timeSlot) {
//        this.timeSlot = timeSlot;
//    }
//
//    public String getExpectedEarning() {
//        return expectedEarning;
//    }
//
//    public void setExpectedEarning(String expectedEarning) {
//        this.expectedEarning = expectedEarning;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeValue(shiftId);
//        dest.writeValue(day);
//        dest.writeValue(date);
//        dest.writeValue(timeSlot);
//        dest.writeValue(expectedEarning);
//        dest.writeValue(role);
//        dest.writeValue(location);
//    }
//
//    public int describeContents() {
//        return  0;
//    }
//
//}
