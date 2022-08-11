package com.testdroid.sample.android.models;

public class DeviceProperty {

    private String name;
    private final String value;

    public DeviceProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

}
