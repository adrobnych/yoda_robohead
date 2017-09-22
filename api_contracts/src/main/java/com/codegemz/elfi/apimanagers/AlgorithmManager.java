package com.codegemz.elfi.apimanagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.codegemz.elfi.apicontracts.AlgorithmContract;
import com.codegemz.elfi.apicontracts.AlgorithmStepContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrobnych on 8/23/15.
 */
public class AlgorithmManager {

    private Context ctx;

    public AlgorithmManager(Context ctx){
        this.ctx = ctx;
    }

    public AlgorithmDTO getAlgorithmById(int id) {
        Cursor cursor = ctx.getContentResolver().query(
                AlgorithmContract.CONTENT_URI, null,
                AlgorithmContract.Columns.ID + " = ?",
                new String[]{"" + id}, null);
        AlgorithmDTO result = new AlgorithmDTO();
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            result.set_id(id);
            result.setName(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.NAME)));
            result.setType(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.TYPE)));
            result.setGroup_name(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.GROUP)));
            result.setAlgorithm_bundle_name(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.ALGORITHM_BUNDLE_NAME)));
            result.setValue1(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.VALUE1)));
            result.setValue2(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.VALUE2)));
        }
        cursor.close();
        return result;
    }

    //TODO: remove id initialization on insert
    public int addAlgorithm(AlgorithmDTO aDTO) {
        ContentValues v = new ContentValues();
//        v.put(AlgorithmContract.Columns.ID,       aDTO.get_id());
        v.put(AlgorithmContract.Columns.NAME,     aDTO.getName());
        v.put(AlgorithmContract.Columns.TYPE,     aDTO.getType());
        v.put(AlgorithmContract.Columns.GROUP,     aDTO.getGroup_name());
        v.put(AlgorithmContract.Columns.VALUE1,     aDTO.getValue1());
        v.put(AlgorithmContract.Columns.VALUE2,     aDTO.getValue2());
        v.put(AlgorithmContract.Columns.ALGORITHM_BUNDLE_NAME,     aDTO.getAlgorithm_bundle_name());

        Uri uri = ctx.getContentResolver().insert(AlgorithmContract.CONTENT_URI, v);
        ctx.getContentResolver().notifyChange(AlgorithmContract.CONTENT_URI, null, true);
        return new Integer(uri.getLastPathSegment()).intValue();
    }

    public void deleteById(int id) {
        AlgorithmDTO aDTO = getAlgorithmById(id);
        if(aDTO != null)
            deleteAllRelatedSteps(aDTO.getName());

        ctx.getContentResolver().delete(AlgorithmContract.CONTENT_URI,
                AlgorithmContract.Columns.ID + " = ?",
                new String[]{"" + id});
        ctx.getContentResolver().notifyChange(AlgorithmContract.CONTENT_URI, null, true);
    }

    private void deleteAllRelatedSteps(String algName) {
        List<AlgorithmStepDTO> steps = (new AlgorithmStepManager(ctx)).getStepsByAlgorithmName(algName);

        AlgorithmStepManager asM = new AlgorithmStepManager(ctx);
        for(AlgorithmStepDTO asDTO : steps)
            asM.deleteById(asDTO.get_id());
    }

    public void deleteAll() {
        ctx.getContentResolver().delete(AlgorithmContract.CONTENT_URI, null, null);
        ctx.getContentResolver().notifyChange(AlgorithmContract.CONTENT_URI, null, true);
    }

    public Cursor getAll() {
        return ctx.getContentResolver().query(
                AlgorithmContract.CONTENT_URI, null,
                null,
                null, null);
    }

    public void updateAlgorithm(AlgorithmDTO aDTO) {
        ContentValues v = new ContentValues();
        v.put(AlgorithmContract.Columns.NAME,     aDTO.getName());
        v.put(AlgorithmContract.Columns.TYPE,     aDTO.getType());
        v.put(AlgorithmContract.Columns.GROUP,     aDTO.getGroup_name());
        v.put(AlgorithmContract.Columns.VALUE1,     aDTO.getValue1());
        v.put(AlgorithmContract.Columns.VALUE2,     aDTO.getValue2());
        v.put(AlgorithmContract.Columns.ALGORITHM_BUNDLE_NAME,     aDTO.getAlgorithm_bundle_name());

        ctx.getContentResolver().update(AlgorithmContract.CONTENT_URI, v,
                AlgorithmContract.Columns.ID + " = ?",
                new String[]{"" + aDTO.get_id()});
        ctx.getContentResolver().notifyChange(AlgorithmContract.CONTENT_URI, null, true);
    }

    public List<AlgorithmStepDTO> getStepsByAlgorithmId(int algorithmId) {
        ArrayList<AlgorithmStepDTO> result = new ArrayList<>();
        Cursor cursor = ctx.getContentResolver().query(
                AlgorithmStepContract.CONTENT_URI, null,
                AlgorithmStepContract.Columns.ALGORITHM_NAME + " = ?",
                new String[]{"" + getAlgorithmById(algorithmId).getName()},
                AlgorithmStepContract.Columns.PRIORITY + " ASC");
        while(cursor.moveToNext()) {
            AlgorithmStepDTO step = new AlgorithmStepDTO();
            step.set_id(cursor.getInt(cursor.getColumnIndex(AlgorithmStepContract.Columns.ID)));
            step.setIntent(cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.INTENT)));
            step.setPriority(cursor.getInt(cursor.getColumnIndex(AlgorithmStepContract.Columns.PRIORITY)));
            step.setValue1(cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.VALUE1)));
            step.setValue2(cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.VALUE2)));
            step.setAlgorithm_name(cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.ALGORITHM_NAME)));
            result.add(step);
        }
        cursor.close();
        return result;
    }

    public AlgorithmDTO getAlgorithmByName(String algorithmName) {
        Cursor cursor = ctx.getContentResolver().query(
                AlgorithmContract.CONTENT_URI, null,
                AlgorithmContract.Columns.NAME + " = ?",
                new String[]{"" + algorithmName}, null);
        AlgorithmDTO result = new AlgorithmDTO();
        if(cursor.getCount() >= 1) {
            cursor.moveToFirst();
            result.set_id(cursor.getInt(cursor.getColumnIndex(AlgorithmContract.Columns.ID)));
            result.setName(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.NAME)));
            result.setType(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.TYPE)));
            result.setGroup_name(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.GROUP)));
            result.setAlgorithm_bundle_name(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.ALGORITHM_BUNDLE_NAME)));
            result.setValue1(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.VALUE1)));
            result.setValue2(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.VALUE2)));
        }
        else
            result = null;
        cursor.close();
        return result;
    }

    public AlgorithmDTO findAlgorithmByV1AndV2(String v1, String v2){
        Cursor cursor = ctx.getContentResolver().query(
                AlgorithmContract.CONTENT_URI, null,
                AlgorithmContract.Columns.VALUE1 + " = ? and " +
                AlgorithmContract.Columns.VALUE2 + " = ?",
                new String[]{v1, v2}, null);
        AlgorithmDTO result = new AlgorithmDTO();
        if(cursor.getCount() >= 1) {
            cursor.moveToFirst();
            result.set_id(cursor.getInt(cursor.getColumnIndex(AlgorithmContract.Columns.ID)));
            result.setName(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.NAME)));
            result.setType(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.TYPE)));
            result.setGroup_name(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.GROUP)));
            result.setAlgorithm_bundle_name(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.ALGORITHM_BUNDLE_NAME)));
            result.setValue1(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.VALUE1)));
            result.setValue2(cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.VALUE2)));
        }
        else
            result = null;
        cursor.close();
        return result;
    }

    public AlgorithmGraph getAlgorithmGraph(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<AlgorithmGraph>(){}.getType();

        AlgorithmGraph ag = gson.fromJson(json, type);
        return ag;
    }

    // json converter to whole graph of Algorithm
    public class AlgorithmGraph{
        public AlgorithmDTO getAlgorithm() {
            return algorithm;
        }

        private AlgorithmDTO algorithm;
        private IndoorLocationDTO fromIndoorLocation;
        private IndoorLocationDTO toIndoorLocation;

        public IndoorLocationDTO getFromIndoorLocation() {
            return fromIndoorLocation;
        }

        public IndoorLocationDTO getToIndoorLocation() {
            return toIndoorLocation;
        }

        public List<AlgorithmStepDTO> getSteps() {
            return steps;
        }

        private List<AlgorithmStepDTO> steps;
    }

    public String getJSON(int id) {

        AlgorithmGraph ag = new AlgorithmGraph();
        prepareAlgorithmGraph(ag, id);

        Gson gson = new Gson();
        Type type = new TypeToken<AlgorithmGraph>(){}.getType();
        String json = gson.toJson(ag, type);
        return json;
    }

    private void prepareAlgorithmGraph(AlgorithmGraph ag, int algorithm_id ) {

        AlgorithmDTO aDTO = getAlgorithmById(algorithm_id);

        ag.algorithm = aDTO;

        if(aDTO.getValue1() != null)
            ag.fromIndoorLocation = (new IndoorLocationManager(ctx)).getLocationByName(aDTO.getValue1());
        if(aDTO.getValue2() != null)
            ag.toIndoorLocation = (new IndoorLocationManager(ctx)).getLocationByName(aDTO.getValue2());

        List<AlgorithmStepDTO> steps = (new AlgorithmStepManager(ctx)).getStepsByAlgorithmName(aDTO.getName());

        if(steps != null && steps.size() !=0)
            ag.steps = steps;
    }
}
