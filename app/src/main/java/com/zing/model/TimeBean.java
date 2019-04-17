package com.zing.model;


public class TimeBean {
    String time;
    boolean isSelected,isAvailableSelected;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public TimeBean() {

    }

    public boolean isAvailableSelected() {
        return isAvailableSelected;
    }

    public void setAvailableSelected(boolean availableSelected) {
        isAvailableSelected = availableSelected;
    }

    public TimeBean(String time, boolean isSelected, boolean isAvailableSelected) {
        this.time = time;
        this.isSelected = isSelected;

        this.isAvailableSelected = isAvailableSelected;
    }
}
