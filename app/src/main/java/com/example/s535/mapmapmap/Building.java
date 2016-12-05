package com.example.s535.mapmapmap;

import java.util.ArrayList;

/**
 * Created by SeongmoPark on 2016-12-04.
 */

public class Building {
    private ArrayList<String> visitors;
    private String buildingname;
    private double latitude, longitude;

    public Building() {}
    public Building(String building_name, double latitude, double longitude)
    {
        this.buildingname = building_name;
        this.latitude=latitude;
        this.longitude=longitude;
        visitors=new ArrayList<String>();

    }

    public ArrayList<String> getVisitors() { return visitors; }
    public String getbuildingName() { return buildingname; }
    public double getbuildingLatitude() {return latitude;}
    public double getbuildingLongitude() {return longitude;}
}