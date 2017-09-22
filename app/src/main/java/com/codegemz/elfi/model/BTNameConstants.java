package com.codegemz.elfi.model;

/**
 * Created by adrobnych on 10/7/15.
 */
public enum BTNameConstants {
    EV3("EV3"), Arduino("HC-06");

    public String getDeviceName() {
        return deviceName;
    }

    private final String deviceName;

    BTNameConstants(String btName) {
        deviceName = btName;
    }

}
