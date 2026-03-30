package com.Shakti.Shakti.Utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateKYCrespose {




    @SerializedName("statuscode")
    @Expose
    private String statuscode;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("isVersionValid")
    @Expose
    private String isVersionValid;
    @SerializedName("isAppValid")
    @Expose
    private String isAppValid;
    @SerializedName("isSessionVaild")
    @Expose
    private String isSessionVaild;
    @SerializedName("isOTPRequired")
    @Expose
    private String isOTPRequired;

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIsVersionValid() {
        return isVersionValid;
    }

    public void setIsVersionValid(String isVersionValid) {
        this.isVersionValid = isVersionValid;
    }

    public String getIsAppValid() {
        return isAppValid;
    }

    public void setIsAppValid(String isAppValid) {
        this.isAppValid = isAppValid;
    }

    public String getIsSessionVaild() {
        return isSessionVaild;
    }

    public void setIsSessionVaild(String isSessionVaild) {
        this.isSessionVaild = isSessionVaild;
    }

    public String getIsOTPRequired() {
        return isOTPRequired;
    }

    public void setIsOTPRequired(String isOTPRequired) {
        this.isOTPRequired = isOTPRequired;
    }


}
