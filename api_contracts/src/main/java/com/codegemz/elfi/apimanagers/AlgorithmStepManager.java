package com.codegemz.elfi.apimanagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.codegemz.elfi.apicontracts.AlgorithmStepContract;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by adrobnych on 8/23/15.
 */
public class AlgorithmStepManager {

    private Context ctx;

    public AlgorithmStepManager(Context ctx){
        this.ctx = ctx;
    }

    public AlgorithmStepDTO getAlgorithmStepById(int id) {
        Cursor cursor = ctx.getContentResolver().query(
                AlgorithmStepContract.CONTENT_URI, null,
                AlgorithmStepContract.Columns.ID + " = ?",
                new String[]{"" + id}, null);
        AlgorithmStepDTO result = new AlgorithmStepDTO();
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            result.set_id(id);
            result.setIntent(cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.INTENT)));
            result.setPriority(cursor.getInt(cursor.getColumnIndex(AlgorithmStepContract.Columns.PRIORITY)));
            result.setValue1(cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.VALUE1)));
            result.setValue2(cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.VALUE2)));
            result.setAlgorithm_name(cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.ALGORITHM_NAME)));
        }
        return result;
    }

    //TODO: remove id initialization on insert
    public int addAlgorithmStep(AlgorithmStepDTO aDTO) {
        ContentValues v = new ContentValues();
//        v.put(AlgorithmStepContract.Columns.ID,       aDTO.get_id());
        v.put(AlgorithmStepContract.Columns.PRIORITY,     aDTO.getPriority());
        v.put(AlgorithmStepContract.Columns.INTENT,     aDTO.getIntent());
        v.put(AlgorithmStepContract.Columns.VALUE1,     aDTO.getValue1());
        v.put(AlgorithmStepContract.Columns.VALUE2,     aDTO.getValue2());
        v.put(AlgorithmStepContract.Columns.ALGORITHM_NAME,     aDTO.getAlgorithm_name());

        Uri uri = ctx.getContentResolver().insert(AlgorithmStepContract.CONTENT_URI, v);
        ctx.getContentResolver().notifyChange(AlgorithmStepContract.CONTENT_URI, null, true);
        return new Integer(uri.getLastPathSegment()).intValue();
    }

    public void deleteById(int id) {
        ctx.getContentResolver().delete(AlgorithmStepContract.CONTENT_URI,
                AlgorithmStepContract.Columns.ID + " = ?",
                new String[]{"" + id});
        ctx.getContentResolver().notifyChange(AlgorithmStepContract.CONTENT_URI, null, true);
    }

    public void deleteAll() {
        ctx.getContentResolver().delete(AlgorithmStepContract.CONTENT_URI, null, null);
        ctx.getContentResolver().notifyChange(AlgorithmStepContract.CONTENT_URI, null, true);
    }

    public void updateAlgorithmStep(AlgorithmStepDTO aDTO) {
        ContentValues v = new ContentValues();
        v.put(AlgorithmStepContract.Columns.PRIORITY,     aDTO.getPriority());
        v.put(AlgorithmStepContract.Columns.INTENT,     aDTO.getIntent());
        v.put(AlgorithmStepContract.Columns.VALUE1,     aDTO.getValue1());
        v.put(AlgorithmStepContract.Columns.VALUE2,     aDTO.getValue2());
        v.put(AlgorithmStepContract.Columns.ALGORITHM_NAME,     aDTO.getAlgorithm_name());

        ctx.getContentResolver().update(AlgorithmStepContract.CONTENT_URI, v,
                AlgorithmStepContract.Columns.ID + " = ?",
                new String[]{"" + aDTO.get_id()});
        ctx.getContentResolver().notifyChange(AlgorithmStepContract.CONTENT_URI, null, true);
    }

    public List<AlgorithmStepDTO> getStepsByAlgorithmName(String algorithmName) {
        Cursor cursor = ctx.getContentResolver().query(
                AlgorithmStepContract.CONTENT_URI, null,
                AlgorithmStepContract.Columns.ALGORITHM_NAME + " = ?",
                new String[]{algorithmName}, null);
        List<AlgorithmStepDTO> result = new LinkedList<>();
        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                AlgorithmStepDTO asBuffer = new AlgorithmStepDTO();
                asBuffer.set_id(cursor.getInt(cursor.getColumnIndex(AlgorithmStepContract.Columns.ID)));
                asBuffer.setIntent(cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.INTENT)));
                asBuffer.setPriority(cursor.getInt(cursor.getColumnIndex(AlgorithmStepContract.Columns.PRIORITY)));
                asBuffer.setValue1(cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.VALUE1)));
                asBuffer.setValue2(cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.VALUE2)));
                asBuffer.setAlgorithm_name(cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.ALGORITHM_NAME)));
                result.add(asBuffer);
            }
        }
        return result;
    }
}
