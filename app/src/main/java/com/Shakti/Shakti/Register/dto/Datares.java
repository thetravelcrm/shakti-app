package com.Shakti.Shakti.Register.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datares {

    String product;
    String deliveryStatus;
    String debit;

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    @SerializedName("categoryName")
    @Expose
    private String categoryName;


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @SerializedName("pdf")
    @Expose
    private String pdf;

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    @SerializedName("purchaseStatus")
    @Expose
    private String purchaseStatus;


    public String getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(String purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    @SerializedName("rewardStatus")
    @Expose
    private String rewardStatus;


    @SerializedName("rewardName")
    @Expose
    private String rewardName;

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    @SerializedName("orderNo")
    @Expose
    private String orderNo;

    @SerializedName("quantity")
    @Expose
    private String quantity;


 @SerializedName("credit")
    @Expose
    private String credit;


    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(String rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


////////////////

    @SerializedName("dealerName")
    @Expose
    private String dealerName;

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    @SerializedName("productName")
    @Expose
    private String productName;



    @SerializedName("jan")
    @Expose
    private String jan;
    @SerializedName("feb")
    @Expose
    private String feb;
    @SerializedName("mar")
    @Expose
    private String mar;
    @SerializedName("apr")
    @Expose
    private String apr;
    @SerializedName("may")
    @Expose
    private String may;
    @SerializedName("jun")
    @Expose
    private String jun;
    @SerializedName("july")
    @Expose
    private String july;
    @SerializedName("aug")
    @Expose
    private String aug;
    @SerializedName("sep")
    @Expose
    private String sep;
    @SerializedName("oct")
    @Expose
    private String oct;
    @SerializedName("nov")
    @Expose
    private String nov;
    @SerializedName("dec")
    @Expose
    private String dec;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getJan() {
        return jan;
    }

    public void setJan(String jan) {
        this.jan = jan;
    }

    public String getFeb() {
        return feb;
    }

    public void setFeb(String feb) {
        this.feb = feb;
    }

    public String getMar() {
        return mar;
    }

    public void setMar(String mar) {
        this.mar = mar;
    }

    public String getApr() {
        return apr;
    }

    public void setApr(String apr) {
        this.apr = apr;
    }

    public String getMay() {
        return may;
    }

    public void setMay(String may) {
        this.may = may;
    }

    public String getJun() {
        return jun;
    }

    public void setJun(String jun) {
        this.jun = jun;
    }

    public String getJuly() {
        return july;
    }

    public void setJuly(String july) {
        this.july = july;
    }

    public String getAug() {
        return aug;
    }

    public void setAug(String aug) {
        this.aug = aug;
    }

    public String getSep() {
        return sep;
    }

    public void setSep(String sep) {
        this.sep = sep;
    }

    public String getOct() {
        return oct;
    }

    public void setOct(String oct) {
        this.oct = oct;
    }

    public String getNov() {
        return nov;
    }

    public void setNov(String nov) {
        this.nov = nov;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }



    @SerializedName("subAdminStatus")
    @Expose
    private String SubAdminStatus;

    @SerializedName("siteStatus")
    @Expose
    private String siteStatus;
    @SerializedName("siteName")
    @Expose
    private String siteName;

    @SerializedName("district")
    @Expose
    private String district;



    @SerializedName("siteType")
    @Expose
    private String siteType;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("startingDate")
    @Expose
    private String startingDate;
    @SerializedName("remark")
    @Expose
    private String remark;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("modifyBy")
    @Expose
    private String modifyBy;

    @SerializedName("modifyDate")
    @Expose
    private String modifyDate;

    public String getSiteStatus() {
        return siteStatus;
    }
    public String getSubAdminStatus() {
        return SubAdminStatus;
    }

    public void setSiteStatus(String siteStatus) {
        this.siteStatus = siteStatus;
    }



    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }



    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }



    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }


    @SerializedName("notification")
    @Expose
    private String notification;

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    @SerializedName("qns")
    @Expose
    private String qns;


    @SerializedName("ans")
    @Expose
    private String ans;


    public String getQns() {
        return qns;
    }

    public void setQns(String qns) {
        this.qns = qns;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    @SerializedName("sitetype")
    @Expose
    private String sitetype;

    @SerializedName("unit")
    @Expose
    private String unit;


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @SerializedName("siteTypeID")
    @Expose
    private String siteTypeID;


    public String getSitetype() {
        return sitetype;
    }

    public void setSitetype(String sitetype) {
        this.sitetype = sitetype;
    }

    public String getSiteTypeID() {
        return siteTypeID;
    }

    public void setSiteTypeID(String siteTypeID) {
        this.siteTypeID = siteTypeID;
    }

    @SerializedName("districtID")
    @Expose
    private String districtID;

    @SerializedName("blockID")
    @Expose
    private String blockID;


    @SerializedName("stateID")
    @Expose
    private String stateID;

    @SerializedName("stateName")
    @Expose
    private String stateName;


    @SerializedName("districtName")
    @Expose
    private String districtName;

    @SerializedName("blockName")
    @Expose
    private String blockName;
    @SerializedName("CompanyName")
    @Expose
    private String CompanyName;
    @SerializedName("agentType")
    @Expose
    private String agentType;
    public String getagentType() {
        return agentType;
    }

    public String getDistrictID() {
        return districtID;
    }
    public String getBlockID() {
        return blockID;
    }
    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public String getStateName() {
        return stateName;
    }

    public String getBlockName() {
        return blockName;
    }
    public String getCompanyName() {
        return CompanyName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }
    public void setBlockID(String blockID) {
        this.blockID = blockID;
    }


    @SerializedName("wish")
    @Expose
    private String wish;


    @SerializedName("userName")
    @Expose
    private String userName;


    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    @SerializedName("purchaseDate")
    @Expose
    private String purchaseDate;

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedDate() {
        return createdDate;
    }
    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("time")
    @Expose
    private String time;


    @SerializedName("points")
    @Expose
    private String points;


    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @SerializedName("image")
    @Expose
    private String image;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("attachment")
    @Expose
    private String attachment;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }



    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("productID")
    @Expose
    private String productID;

    @SerializedName("mobile")
    @Expose
    private String mobile;


    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


}

