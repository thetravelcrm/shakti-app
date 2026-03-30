package com.Shakti.Shakti.ApisetRespose;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiBodyParam {



    @SerializedName("APPID")
    @Expose
    private String aPPID;
    @SerializedName("IMEI")
    @Expose
    private String iMEI;
    @SerializedName("RegKey")
    @Expose
    private String regKey;
    @SerializedName("Version")
    @Expose
    private String version;

    @SerializedName("StateID")
    @Expose
    private String stateID;

    @SerializedName("id")
    @Expose
    private String ID;

    @SerializedName("UserID")
    @Expose
    private String UserID;


    @SerializedName("session")
    @Expose
    private String session;


    public void setSession(String session) {
        this.session = session;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public void setUserID(String userID) {
        UserID = userID;
    }

    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("userCode")
    @Expose
    private String userCode;
    @SerializedName("fName")
    @Expose
    private String fName;
    @SerializedName("lName")
    @Expose
    private String lName;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("whatsappNo")
    @Expose
    private String whatsappNo;
    @SerializedName("emailID")
    @Expose
    private String emailID;
    @SerializedName("workingStateID")
    @Expose
    private String workingStateID;
    @SerializedName("workingDistrictID")
    @Expose
    private String workingDistrictID;
    @SerializedName("workingBlockID")
    @Expose
    private String workingBlockID;
    @SerializedName("dealer1ID")
    @Expose
    private String dealer1ID;
    @SerializedName("dealer2ID")
    @Expose
    private String dealer2ID;
    @SerializedName("dealer3ID")
    @Expose
    private String dealer3ID;
    @SerializedName("officeAddress")
    @Expose
    private String officeAddress;
    @SerializedName("officePINCODE")
    @Expose
    private String officePINCODE;
    @SerializedName("officeCity")
    @Expose
    private String officeCity;
    @SerializedName("officeStateID")
    @Expose
    private String officeStateID;
    @SerializedName("officeDistrictID")
    @Expose
    private String officeDistrictID;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("anniversary")
    @Expose
    private String anniversary;
    @SerializedName("aadhaarNumber")
    @Expose
    private String aadhaarNumber;
    @SerializedName("aadhaarPhoto")
    @Expose
    private String aadhaarPhoto;
    @SerializedName("gstNumber")
    @Expose
    private String gstNumber;
    @SerializedName("officeLat")
    @Expose
    private String officeLat;
    @SerializedName("officeLong")
    @Expose
    private String officeLong;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("panNumber")
    @Expose
    private String panNumber;
    @SerializedName("companyContactName")
    @Expose
    private String companyContactName;
    @SerializedName("agentType")
    @Expose
    private String agentType;
    @SerializedName("approveStatus")
    @Expose
    private String approveStatus;


    public void setaPPID(String aPPID) {
        this.aPPID = aPPID;
    }

    public void setiMEI(String iMEI) {
        this.iMEI = iMEI;
    }

    public void setRegKey(String regKey) {
        this.regKey = regKey;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
    public String getfName()
    {
        return fName;
    }
    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setWhatsappNo(String whatsappNo) {
        this.whatsappNo = whatsappNo;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setWorkingStateID(String workingStateID) {
        this.workingStateID = workingStateID;
    }

    public void setWorkingDistrictID(String workingDistrictID) {
        this.workingDistrictID = workingDistrictID;
    }
    public void setWorkingBlockID(String workingBlockID) {
        this.workingBlockID = workingBlockID;
    }
    public void setDealer1ID(String dealer1ID) {
        this.dealer1ID = dealer1ID;
    }

    public void setDealer2ID(String dealer2ID) {
        this.dealer2ID = dealer2ID;
    }

    public void setDealer3ID(String dealer3ID) {
        this.dealer3ID = dealer3ID;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public void setOfficePINCODE(String officePINCODE) {
        this.officePINCODE = officePINCODE;
    }

    public void setOfficeCity(String officeCity) {
        this.officeCity = officeCity;
    }

    public void setOfficeStateID(String officeStateID) {
        this.officeStateID = officeStateID;
    }

    public void setOfficeDistrictID(String officeDistrictID) {
        this.officeDistrictID = officeDistrictID;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setAnniversary(String anniversary) {
        this.anniversary = anniversary;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public void setAadhaarPhoto(String aadhaarPhoto) {
        this.aadhaarPhoto = aadhaarPhoto;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public void setOfficeLat(String officeLat) {
        this.officeLat = officeLat;
    }

    public void setOfficeLong(String officeLong) {
        this.officeLong = officeLong;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public void setCompanyContactName(String companyContactName) {
        this.companyContactName = companyContactName;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }





}
