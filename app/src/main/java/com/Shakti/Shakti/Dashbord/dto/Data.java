
package com.Shakti.Shakti.Dashbord.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("earned")
    @Expose
    private String earned;
    @SerializedName("redeemed")
    @Expose
    private String redeemed;
    @SerializedName("remaining")
    @Expose
    private String remaining;

    public String getEarned() {
        return earned;
    }

    public void setEarned(String earned) {
        this.earned = earned;
    }

    public String getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(String redeemed) {
        this.redeemed = redeemed;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

}
