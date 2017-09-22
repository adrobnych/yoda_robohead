package com.codegemz.elfi.model;

import android.provider.BaseColumns;

import com.codegemz.elfi.apicontracts.IndoorLocationContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation;

@DatabaseTable(tableName = IndoorLocationContract.TABLE_NAME)
@AdditionalAnnotation.DefaultContentUri(authority = IndoorLocationContract.AUTHORITY, path = IndoorLocationContract.CONTENT_URI_PATH)
@AdditionalAnnotation.DefaultContentMimeTypeVnd(name = IndoorLocationContract.MIMETYPE_NAME, type = IndoorLocationContract.MIMETYPE_TYPE)
public class IndoorLocation {

    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @AdditionalAnnotation.DefaultSortOrder
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String wifi_ssid;

    @DatabaseField
    private float wifi_correction_coefficient;


    public IndoorLocation() {
        // ORMLite needs a no-arg constructor
    }

    public IndoorLocation(int id, String name, String wifi_ssid) {
        this.id = id;
        this.name = name;
        this.wifi_ssid = wifi_ssid;
    }

    public String getName() {
        return name;
    }

    public String getWifiSsid() {
        return wifi_ssid;
    }
}

