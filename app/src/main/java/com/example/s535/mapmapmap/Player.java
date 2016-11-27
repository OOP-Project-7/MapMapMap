package com.example.s535.mapmapmap;

/**
 * Created by S535 on 2016-11-27.
 */

public class Player
{
    private String userID;
    private String statusMessage;
    private double lat;
    private double lng;
    private int footType;
    private int footColor;
    public Player(String userID, String statusMessage, double lat, double lng,int footType, int footColor)
    {
        this.userID=userID;
        this.statusMessage=statusMessage;
        this.lat=lat;
        this.lng=lng;
        this.footType=footType;
        this.footColor=footColor;
    }
    public void setLat(double lat)
    {
        this.lat=lat;
    }
    public void setLng(double lng)
    {
        this.lng=lng;
    }
    public double getLat()
    {
        return this.lat;
    }
    public double getLng()
    {
        return this.lng;
    }
    public void setfootType(int footType)
    {
        this.footType=footType;
    }
    public int getfootType()
    {
        return this.footType;
    }
    public void setfootColor(int footColor)
    {
        this.footColor=footColor;
    }
    public int getfootColor()
    {
        return this.footColor;
    }
}