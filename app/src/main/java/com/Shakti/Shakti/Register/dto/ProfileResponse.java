package com.Shakti.Shakti.Register.dto;

import com.Shakti.Shakti.ApisetRespose.ApiBodyParam;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProfileResponse
{
    @SerializedName("list")
    @Expose
    private ArrayList<ProfileList> list=null;

    public ArrayList<ProfileList> getList(){ return  list;}
    public void setList(ArrayList<ProfileList> list) {
        this.list = list;
    }

    @SerializedName("statuscode")
    @Expose
    private String status;
    public String getStatus() {
        return status;
    }

    @SerializedName("msg")
    @Expose
    private String msg;
    public String getMsg() {
        return msg;
    }
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
    public String getIsVersionValid() {
        return isVersionValid;
    }

    public String getIsAppValid() {
        return isAppValid;
    }

    public String getIsSessionVaild() {
        return isSessionVaild;
    }

    public String getIsOTPRequired() {
        return isOTPRequired;
    }

    public int getItemCount() {
        return list.size();
    }
    public class ProfileList{
        @SerializedName("id")
        @Expose
        private String ID;
        public String getID(){return ID;}

        @SerializedName("fName")
        @Expose
        private String fName;
        public String getfName(){return fName;}

        @SerializedName("lName")
        @Expose
        private String lName;
        public String getlName(){return lName;}

        @SerializedName("mobileNo")
        @Expose
        private String MobileNumber;
        public String getMobileNumber(){return MobileNumber;}
        @SerializedName("userType")
        @Expose
        private String userType;
        public String getuserType(){return userType;}
        @SerializedName("userCode")
        @Expose
        private String userCode;
        public String getuserCode(){return userCode;}
        @SerializedName("whatsappNo")
        @Expose
        private String whatsappNo;
        public String getwhatsappNo(){return whatsappNo;}
        @SerializedName("emailID")
        @Expose
        private String emailID;
        public String getemailID(){return emailID;}
        @SerializedName("workingStateID")
        @Expose
        private String workingStateID;
        public String getworkingStateID(){return workingStateID;}
        @SerializedName("workingState")
        @Expose
        private String workingState;
        public String getworkingState(){return workingState;}

        @SerializedName("workingDistrictID")
        @Expose
        private String workingDistrictID;
        public String getworkingDistrictID(){return workingDistrictID;}
        @SerializedName("workingDistrict")
        @Expose
        private String workingDistrict;
        public String getworkingDistrict(){return workingDistrict;}

        @SerializedName("workingBlockID")
        @Expose
        private String workingBlockID;
        public String getworkingBlockID(){return workingBlockID;}
        @SerializedName("workingBlock")
        @Expose
        private String workingBlock;
        public String getworkingBlock(){return workingBlock;}
        @SerializedName("officeAddress")
        @Expose
        private String officeAddress;
        public String getofficeAddress(){return officeAddress;}
        @SerializedName("dob")
        @Expose
        private String dob;
        public String getdob(){return dob;}
        @SerializedName("anniversary")
        @Expose
        private String anniversary;
        public String getanniversary(){return anniversary;}
        @SerializedName("aadhaarNumber")
        @Expose
        private String aadhaarNumber;
        public String getaadhaarNumber(){return aadhaarNumber;}
        @SerializedName("aadhaarPhoto")
        @Expose
        private String aadhaarPhoto;
        public String getaadhaarPhoto(){
            if(aadhaarPhoto.equalsIgnoreCase("/images/user/1.png"))
            {
                aadhaarPhoto= ApplicationConstant.INSTANCE.baseUrl+aadhaarPhoto;
            }
            return aadhaarPhoto;
        }
        @SerializedName("aadhaarBackPhoto")
        @Expose
        private String aadhaarBackPhoto;
        public String getaadhaarBackPhoto(){
            if(aadhaarBackPhoto.equalsIgnoreCase("/images/user/1.png"))
            {
                aadhaarBackPhoto= ApplicationConstant.INSTANCE.baseUrl+aadhaarBackPhoto;
            }
            return aadhaarBackPhoto;
        }
        @SerializedName("gstNumber")
        @Expose
        private String gstNumber;
        public String getgstNumber(){return gstNumber;}
        @SerializedName("panNumber")
        @Expose
        private String panNumber;
        public String getpanNumber(){return panNumber;}
        @SerializedName("companyContactName")
        @Expose
        private String companyContactName;
        public String getcompanyContactName(){return companyContactName;}
        @SerializedName("agentType")
        @Expose
        private String agentType;
        public String getagentType(){return agentType;}
        @SerializedName("photo")
        @Expose
        private String Photo;
        public String getPhoto(){return Photo;}
    }
}
