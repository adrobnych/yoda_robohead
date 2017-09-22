package com.codegemz.elfi.apimanagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.codegemz.elfi.apicontracts.IndoorLocationContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;

/**
 * Created by adrobnych on 8/23/15.
 */
public class IndoorLocationManager {

    private Context ctx;
    private Cursor all;

    public IndoorLocationManager(Context ctx){
        this.ctx = ctx;
    }

    public IndoorLocationDTO getIndoorLocationById(int id) {
        Cursor cursor = ctx.getContentResolver().query(
                IndoorLocationContract.CONTENT_URI, null,
                IndoorLocationContract.Columns.ID + " = ?",
                new String[]{"" + id}, null);
        IndoorLocationDTO result = new IndoorLocationDTO();
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            result.set_id(id);
            result.setName(cursor.getString(cursor.getColumnIndex(IndoorLocationContract.Columns.NAME)));
            result.setWifi_ssid(cursor.getString(cursor.getColumnIndex(IndoorLocationContract.Columns.WIFI_SSID)));
            result.setWifi_correction_coefficient(cursor.getFloat(cursor.getColumnIndex(IndoorLocationContract.Columns.WIFI_CORRECTION_COEFFICIENT)));
        }
        return result;
    }

    public int addIndoorLocation(IndoorLocationDTO aDTO) {
        ContentValues v = new ContentValues();
//        v.put(AlgorithmContract.Columns.ID,       aDTO.get_id());
        v.put(IndoorLocationContract.Columns.NAME,     aDTO.getName());
        v.put(IndoorLocationContract.Columns.WIFI_SSID,     aDTO.getWifi_ssid());
        v.put(IndoorLocationContract.Columns.WIFI_CORRECTION_COEFFICIENT,     aDTO.getWifi_correction_coefficient());

        Uri uri = ctx.getContentResolver().insert(IndoorLocationContract.CONTENT_URI, v);
        ctx.getContentResolver().notifyChange(IndoorLocationContract.CONTENT_URI, null, true);
        return new Integer(uri.getLastPathSegment()).intValue(); //return id of new record
    }

    public void deleteAll() {
        ctx.getContentResolver().delete(IndoorLocationContract.CONTENT_URI, null, null);
    }

    public void deleteById(int id) {
        ctx.getContentResolver().delete(IndoorLocationContract.CONTENT_URI,
                IndoorLocationContract.Columns.ID + " = ?",
                new String[]{"" + id});
    }

    public Cursor getAll() {
        return ctx.getContentResolver().query(
                IndoorLocationContract.CONTENT_URI, null,
                null,
                null, null);
    }

    public String getLocationNameByWifi(String wifiName) {
        Cursor cursor = ctx.getContentResolver().query(
                IndoorLocationContract.CONTENT_URI, null,
                IndoorLocationContract.Columns.WIFI_SSID + " = ?",
                new String[]{"" + wifiName}, null);
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(IndoorLocationContract.Columns.NAME));
        }
        return null;

    }

    public void updateIndoorLocation(IndoorLocationDTO aDTO) {
        ContentValues v = new ContentValues();
        v.put(IndoorLocationContract.Columns.NAME,     aDTO.getName());
        v.put(IndoorLocationContract.Columns.WIFI_SSID,     aDTO.getWifi_ssid());
        v.put(IndoorLocationContract.Columns.WIFI_CORRECTION_COEFFICIENT,     aDTO.getWifi_correction_coefficient());

        ctx.getContentResolver().update(IndoorLocationContract.CONTENT_URI, v,
                IndoorLocationContract.Columns.ID + " = ?",
                new String[]{"" + aDTO.get_id()});
        ctx.getContentResolver().notifyChange(IndoorLocationContract.CONTENT_URI, null, true);
    }

    public IndoorLocationDTO getLocationByName(String ilName) {
        Cursor cursor = ctx.getContentResolver().query(
                IndoorLocationContract.CONTENT_URI, null,
                IndoorLocationContract.Columns.NAME + " = ?",
                new String[]{ilName}, null);
        IndoorLocationDTO result = null;
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            result = new IndoorLocationDTO();
            result.set_id(cursor.getInt(cursor.getColumnIndex(IndoorLocationContract.Columns.ID)));
            result.setName(cursor.getString(cursor.getColumnIndex(IndoorLocationContract.Columns.NAME)));
            result.setWifi_ssid(cursor.getString(cursor.getColumnIndex(IndoorLocationContract.Columns.WIFI_SSID)));
            result.setWifi_correction_coefficient(cursor.getFloat(cursor.getColumnIndex(IndoorLocationContract.Columns.WIFI_CORRECTION_COEFFICIENT)));
        }
        return result;
    }
}
