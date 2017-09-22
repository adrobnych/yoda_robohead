package com.codegemz.elfi.apimanagers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.codegemz.elfi.apicontracts.StateContract;

/**
 * Created by adrobnych on 4/2/16.
 */
public class StateManager {

    private Context context;

    public StateManager(Context context) {
        this.context = context;
    }

    public int createState(StateDTO sDTO){

        Uri uri = context.getContentResolver().insert(StateContract.CONTENT_URI, prepareContentValues(sDTO));

        return (int) ContentUris.parseId(uri);
    }

    public ContentValues prepareContentValues(StateDTO sDTO) {
        ContentValues v = new ContentValues(1);
        v.put(StateContract.Columns.NAME, sDTO.getName());
        v.put(StateContract.Columns.VALUE1, sDTO.getValue1());
        v.put(StateContract.Columns.VALUE2, sDTO.getValue2());
        return v;
    }

    public void deleteStateByName(String name) {
        context.getContentResolver().delete(StateContract.CONTENT_URI,
                StateContract.Columns.NAME + " = ?",
                new String[]{name});
        context.getContentResolver().notifyChange(StateContract.CONTENT_URI, null, true);
    }
}
