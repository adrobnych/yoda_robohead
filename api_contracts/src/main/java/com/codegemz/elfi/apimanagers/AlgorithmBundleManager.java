package com.codegemz.elfi.apimanagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.codegemz.elfi.apicontracts.AlgorithmBundleContract;

import java.util.List;

/**
 * Created by adrobnych on 6/1/15.
 */
public class AlgorithmBundleManager {

    private Context ctx;
    private Cursor all;

    public AlgorithmBundleManager(Context ctx){
        this.ctx = ctx;
    }

    public AlgorithmBundleDTO getAlgorithmBundleById(int id) {
        Cursor cursor = ctx.getContentResolver().query(
                AlgorithmBundleContract.CONTENT_URI, null,
                AlgorithmBundleContract.Columns.ID + " = ?",
                new String[]{"" + id}, null);
        AlgorithmBundleDTO result = new AlgorithmBundleDTO();
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            result.set_id(id);
            result.setName(cursor.getString(cursor.getColumnIndex(AlgorithmBundleContract.Columns.NAME)));
            result.setUrl(cursor.getString(cursor.getColumnIndex(AlgorithmBundleContract.Columns.URL)));
        }
        return result;
    }

    public AlgorithmBundleDTO getAlgorithmBundleByPosition(int position) {
        return getAlgorithmBundleById(position);
    }

    public void clearAll() {
        ctx.getContentResolver().delete(AlgorithmBundleContract.CONTENT_URI, null, null);
    }

    //TODO: remove id initialization on insert
    public int addAlgorithmBundle(AlgorithmBundleDTO abDTO) {
        ContentValues v = new ContentValues();
        //v.put(AlgorithmBundleContract.Columns.ID,       abDTO.get_id());
        v.put(AlgorithmBundleContract.Columns.NAME,     abDTO.getName());
        v.put(AlgorithmBundleContract.Columns.URL,      abDTO.getUrl());
        Uri uri = ctx.getContentResolver().insert(AlgorithmBundleContract.CONTENT_URI, v);
        //ctx.getContentResolver().notifyChange(AlgorithmBundleContract.CONTENT_URI, null, true);
        return new Integer(uri.getLastPathSegment()).intValue();
    }

    public void deleteById(int id) {
        ctx.getContentResolver().delete(AlgorithmBundleContract.CONTENT_URI,
                AlgorithmBundleContract.Columns.ID + " = ?",
                new String[]{"" + id});
    }

    public void deleteAll() {
        ctx.getContentResolver().delete(AlgorithmBundleContract.CONTENT_URI, null, null);
    }

    public Cursor getAll() {
        return ctx.getContentResolver().query(
                AlgorithmBundleContract.CONTENT_URI, null,
                null,
                null, null);
    }

    public void updateAlgorithmBundle(AlgorithmBundleDTO abDTO) {
        ContentValues v = new ContentValues();
        v.put(AlgorithmBundleContract.Columns.NAME,     abDTO.getName());
        v.put(AlgorithmBundleContract.Columns.URL,      abDTO.getUrl());

        ctx.getContentResolver().update(AlgorithmBundleContract.CONTENT_URI, v,
                AlgorithmBundleContract.Columns.ID + " = ?",
                new String[]{"" + abDTO.get_id()});
        //ctx.getContentResolver().notifyChange(AlgorithmBundleContract.CONTENT_URI, null, true);
    }
}
