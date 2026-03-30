package com.Shakti.Shakti.Register.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RegisterResponse {

    @SerializedName("redeem")
    @Expose
    private ArrayList<Datares> redeem = null;

    @SerializedName("earn")
    @Expose
    private ArrayList<Datares> earn = null;

    public ArrayList<Datares> getEarn() {
        return earn;
    }

    public void setEarn(ArrayList<Datares> earn) {
        this.earn = earn;
    }

    public ArrayList<Datares> getRedeem() {
        return redeem;
    }

    public void setRedeem(ArrayList<Datares> redeem) {
        this.redeem = redeem;
    }

    @SerializedName("status")
    @Expose
    private String status;


    @SerializedName("userCode")
    @Expose
    private String userCode;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @SerializedName("otp")
    @Expose
    private String otp;
   
    @SerializedName("session")
    @Expose
    private String session;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("userRole")
    @Expose
    private String userRole;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("role")
    @Expose
    private String role;


    @SerializedName("photo")
    @Expose
    private String photo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    


    @SerializedName("data")
    @Expose
    private ArrayList<Datares> data = null;


     @SerializedName("list")
    @Expose
    private ArrayList<Datares> list = null;




    public ArrayList<Datares> getList() {
        return list;
    }


    public void setList(ArrayList<Datares> list) {
        this.list = list;
    }

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

    public ArrayList<Datares> getData() {
        return data;
    }

    public void setData(ArrayList<Datares> data) {
        this.data = data;
    }

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