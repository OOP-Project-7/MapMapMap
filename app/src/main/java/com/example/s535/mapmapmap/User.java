package com.example.s535.mapmapmap;

import java.io.Serializable;

/**
 * Created by SeongmoPark on 2016-11-28.
 */

public class User implements Serializable {
    private String user_id;
    private String foot_type;
    private String foot_color;
    private String tag_type;
    private String year;
    private String month;
    private String day;
    private String statusmessage;
    private String latitude;
    private String longitude;

    public User() {}
    public User(String user_id, String foot_type, String foot_color, String tag_type, String year, String month, String day, String statusmessage, String latitude, String longitude) {
        this.user_id = user_id;
        this.foot_type = foot_type;
        this.foot_color = foot_color;
        this.tag_type = tag_type;
        this.year = year;
        this.month = month;
        this.day = day;
        this.statusmessage = statusmessage;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public String getUser_id() { return user_id;}
    public int getFootType()
    {
        return Integer.parseInt(foot_type);
    }
    public int getFootColor()
    {
        return Integer.parseInt(foot_color);
    }
    public int getTagType()
    {
        return Integer.parseInt(tag_type);
    }
    public String getBirthDay()
    {
        return year+month+day;
    }
    public int getYear() {return Integer.parseInt(year);}
    public int getMonth() {return Integer.parseInt(month);}
    public int getDay() {return Integer.parseInt(day);}
    public String getStatusmessage()
    {
        return statusmessage;
    }
    public double getLatitude()
    {
        return Double.parseDouble(latitude);
    }
    public double getLongitude()
    {
        return Double.parseDouble(longitude);
    }
}