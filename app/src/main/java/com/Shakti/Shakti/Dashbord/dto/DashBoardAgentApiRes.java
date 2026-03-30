
package com.Shakti.Shakti.Dashbord.dto;

import java.util.ArrayList;
import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashBoardAgentApiRes {

    @SerializedName("data")
    @Expose
    private Data data;
    

    @SerializedName("siteList")
    @Expose
    private ArrayList<ProductList> siteList = null;

    @SerializedName("purchaseList")
    @Expose
    private ArrayList<ProductList> purchaseList = null;

    @SerializedName("saleList")
    @Expose
    private ArrayList<ProductList> saleList = null;

    @SerializedName("redemptionList")
    @Expose
    private ArrayList<ProductList> redemptionList = null;


    public ArrayList<ProductList> getPurchaseList() {
        return purchaseList;
    }
    public ArrayList<ProductList> getSaleList() {
        return saleList;
    }

    public void setPurchaseList(ArrayList<ProductList> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public ArrayList<ProductList> getRedemptionList() {
        return redemptionList;
    }

    public void setRedemptionList(ArrayList<ProductList> redemptionList) {
        this.redemptionList = redemptionList;
    }

    @SerializedName("productList")
    @Expose
    private ArrayList<ProductList> productList = null;
    
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

   

    public ArrayList<ProductList> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductList> productList) {
        this.productList = productList;
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


    public ArrayList<ProductList> getSiteList() {
        return siteList;
    }

    public void setSiteList(ArrayList<ProductList> siteList) {
        this.siteList = siteList;
    }
}
