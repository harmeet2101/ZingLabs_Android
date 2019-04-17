
package com.zing.model.response.StatsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Badge {

    @SerializedName("badge_img_url")
    @Expose
    private String badgeImgUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("badge_id")
    @Expose
    private String badgeId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("badge_status")
    @Expose
    private String badgeStatus;

    public String getBadgeImgUrl() {
        return badgeImgUrl;
    }

    public void setBadgeImgUrl(String badgeImgUrl) {
        this.badgeImgUrl = badgeImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBadgeStatus() {
        return badgeStatus;
    }

    public void setBadgeStatus(String badgeStatus) {
        this.badgeStatus = badgeStatus;
    }

}
