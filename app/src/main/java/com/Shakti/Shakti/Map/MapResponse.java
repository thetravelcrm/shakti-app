package com.Shakti.Shakti.Map;
import com.Shakti.Shakti.Register.dto.ProfileResponse;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
public class MapResponse {

    @SerializedName("list")
    @Expose
    private ArrayList<MapResponse.MapList> list=null;

    public ArrayList<MapResponse.MapList> getList(){ return  list;}
    public void setList(ArrayList<MapResponse.MapList> list) {
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

    @SerializedName("agents")
    @Expose
    private String Agents;
    public String getAgents() {
        return Agents;
    }

    @SerializedName("dealers")
    @Expose
    private String Dealers;
    public String getDealers() {
        return Dealers;
    }

    @SerializedName("sites")
    @Expose
    private String Sites;
    public String getSites() {
        return Sites;
    }

    public int getItemCount() {
        return list.size();
    }

    public class MapList{
        @SerializedName("mapType")
        @Expose
        private String MapType;
        public String getMapType(){return MapType;}

        @SerializedName("title")
        @Expose
        private String Title;
        public String getTitle(){return Title;}

        @SerializedName("snippet")
        @Expose
        private String Snippet;
        public String getSnippet(){return Snippet;}

        @SerializedName("lat")
        @Expose
        private String Lat;
        public String getLat(){return Lat;}

        @SerializedName("long")
        @Expose
        private String Long;
        public String getLong(){return Long;}

    }
}
