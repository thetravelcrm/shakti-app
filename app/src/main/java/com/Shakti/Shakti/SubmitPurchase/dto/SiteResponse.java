package com.Shakti.Shakti.SubmitPurchase.dto;

import com.Shakti.Shakti.Register.dto.ProfileResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SiteResponse {
    @SerializedName("list")
    @Expose
    private ArrayList<SiteList> list=null;

    public ArrayList<SiteList> getList(){ return  list;}
    public void setList(ArrayList<SiteList> list) {
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

    public int getItemCount() {
        return list.size();
    }
    public class SiteList{
        @SerializedName("stateName")
        @Expose
        private String stateName;
        @SerializedName("districtName")
        @Expose
        private String districtName;
        @SerializedName("blockName")
        @Expose
        private String blockName;
        @SerializedName("siteType")
        private String siteType;
        @SerializedName("constructionStage")
        private String constructionStage;
        @SerializedName("dealerName")
        private String dealerName;
        @SerializedName("agentName")
        private String agentName;
        @SerializedName("agentMobile")
        private String agentMobile;
        @SerializedName("ownerName")
        private String ownerName;
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("address")
        private String address;
        @SerializedName("startDate")
        private String startDate;
        @SerializedName("Brand")
        private String brand;
        @SerializedName("BagRequired")
        private String bagRequired;
        @SerializedName("siteStatus")
        private String siteStatus;
        @SerializedName("siteRemark")
        private String siteRemark;
        @SerializedName("photo")
        private String photo;
        @SerializedName("subAdminStatus")
        private String subAdminStatus;
        @SerializedName("subAdminRemark")
        private String subAdminRemark;
        @SerializedName("VarifyBy")
        private String varifyBy;
        @SerializedName("lat")
        private String lat;
        @SerializedName("long")
        private String _long;
        @SerializedName("latAgent")
        private String latAgent;
        @SerializedName("longAgent")
        private String longAgent;
        @SerializedName("SubAdminID")
        private String subAdminID;
        @SerializedName("DistrictID")
        private String districtID;
        @SerializedName("SiteTypeID")
        private String siteTypeID;
        @SerializedName("ConsStageID")
        private String consStageID;
        @SerializedName("DealerID")
        private String dealerID;
        @SerializedName("BrandID")
        private String brandID;
        @SerializedName("id")
        private String iD;
        @SerializedName("Status")
        private Boolean status;
        @SerializedName("CreatedBy")
        private String createdBy;
        @SerializedName("ModifyBy")
        private String modifyBy;
        @SerializedName("CreatedDate")
        private String createdDate;
        @SerializedName("ModifyDate")
        private String modifyDate;

        public String getStateName() {
            return stateName;
        }

        public String getDistrictName() {
            return districtName;
        }


        public String getBlockName() {
            return blockName;
        }


        public String getSiteType() {
            return siteType;
        }

        public String getConstructionStage() {
            return constructionStage;
        }

        public String getDealerName() {
            return dealerName;
        }

        public String getAgentName() {
            return agentName;
        }

        public String getAgentMobile() {
            return agentMobile;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public String getMobile() {
            return mobile;
        }

        public String getAddress() {
            return address;
        }

        public String getStartDate() {
            if(startDate.equalsIgnoreCase("1/1/1900 12:00:00 AM"))
            {
                return "";
            }
            else
            return startDate;
        }

        public String getBrand() {
            return brand;
        }
        public String getBagRequired() {
            return bagRequired;
        }


        public String getSiteStatus() {
            return siteStatus;
        }


        public String getSiteRemark() {
            return siteRemark;
        }

        public String getPhoto() {
            return photo;
        }

        public String getSubAdminStatus() {
            return subAdminStatus;
        }
        public String getSubAdminRemark() {
            return subAdminRemark;
        }
        public String getVarifyBy() {
            return varifyBy;
        }

        public String getLat() {
            return lat;
        }

        public String getLong() {
            return _long;
        }

        public String getLatAgent() {
            return latAgent;
        }


        public String getLongAgent() {
            return longAgent;
        }


        public String getSubAdminID() {
            return subAdminID;
        }


        public String getDistrictID() {
            return districtID;
        }

        public String getSiteTypeID() {
            return siteTypeID;
        }




    }
}
