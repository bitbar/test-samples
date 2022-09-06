package com.testdroid.sample.android.models;

import java.util.ArrayList;

public class DevicePropertyGroup {

    private String name;
    private final ArrayList<DeviceProperty> propertyList;

    public DevicePropertyGroup(String label, ArrayList<DeviceProperty> propertyList) {
        this.name = label;
        this.propertyList = propertyList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<DeviceProperty> getPropertyList() {
        return propertyList;
    }

}
