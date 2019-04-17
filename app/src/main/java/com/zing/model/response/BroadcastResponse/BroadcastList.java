
package com.zing.model.response.BroadcastResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BroadcastList {

    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("other_link")
    @Expose
    private String otherLink;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("type")
    @Expose
    private String type;

    public Integer getIsMsgPersonal() {
        return isMsgPersonal;
    }

    public void setIsMsgPersonal(Integer isMsgPersonal) {
        this.isMsgPersonal = isMsgPersonal;
    }

    @SerializedName("is_personal")
    @Expose
    private Integer isMsgPersonal;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOtherLink() {
        return otherLink;
    }

    public void setOtherLink(String otherLink) {
        this.otherLink = otherLink;
    }

}
