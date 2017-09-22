package com.codegemz.elfi.apimanagers;

/**
 * Created by adrobnych on 8/23/15.
 */
public class IndoorLocationDTO {
    private int _id;
    private String name;
    private String wifi_ssid;
    private float wifi_correction_coefficient;

    public IndoorLocationDTO(String name, String wifi_ssid, float wifi_correction_coefficient) {
        this.name = name;
        this.wifi_ssid = wifi_ssid;
        this.wifi_correction_coefficient = wifi_correction_coefficient;
    }

    public IndoorLocationDTO() {
        this.name = null;
        this.wifi_ssid = null;
        this.wifi_correction_coefficient = (float)1.0;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWifi_ssid() {
        return wifi_ssid;
    }

    public void setWifi_ssid(String wifi_ssid) {
        this.wifi_ssid = wifi_ssid;
    }

    public float getWifi_correction_coefficient() {
        return wifi_correction_coefficient;
    }

    public void setWifi_correction_coefficient(float wifi_correction_coefficient) {
        this.wifi_correction_coefficient = wifi_correction_coefficient;
    }
}
