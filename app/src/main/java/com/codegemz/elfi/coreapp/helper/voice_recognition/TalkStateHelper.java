package com.codegemz.elfi.coreapp.helper.voice_recognition;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.codegemz.elfi.apicontracts.StateContract;
import com.codegemz.elfi.model.TalkStateType;

/**
 * Created by adrobnych on 5/26/15.
 */
public class TalkStateHelper {

    Context context;
    ContentResolver cr;

    public TalkStateHelper(Context context){
        this.context = context;
        cr = context.getContentResolver();
    }

    public TalkStateType getTalkState() {
        Cursor cursor = cr.query(    StateContract.CONTENT_URI, null,
                "name = ?",
                new String[]{"talk"}, null);
        cursor.moveToFirst();
        TalkStateType result;
        if(cursor.getCount() == 0)
            result = null;
        else
            result = TalkStateType.valueOf(cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE1)));
        cursor.close();
        return result;
    }

    public void setTalkSate(TalkStateType tst) {
        //Log.d("TalkStateHelper", "received for set: " + tst);
        ContentValues v = new ContentValues();
        v.put(StateContract.Columns.NAME, "talk");
        v.put(StateContract.Columns.VALUE1, tst.toString());
        v.put(StateContract.Columns.VALUE2, "" + System.currentTimeMillis());

        //test that there is no talk records
        Cursor cursor = cr.query(    StateContract.CONTENT_URI, null,
                "name = ?",
                new String[]{"talk"}, null);
        if(cursor.getCount() == 0)
            cr.insert(StateContract.CONTENT_URI, v);
        else
            cr.update(StateContract.CONTENT_URI, v, "name = ?", new String[]{"talk"});
        cursor.close();
    }

    public long getTalkStateLastTimeUpdate() {
        Cursor cursor = cr.query(    StateContract.CONTENT_URI, null,
            "name = ?",
            new String[]{"talk"}, null);
        cursor.moveToFirst();
        long result;
        if(cursor.getCount() == 0)
            result = -1;
        else
            result = cursor.getLong(cursor.getColumnIndex(StateContract.Columns.VALUE2));
        cursor.close();
        return result;
    }
}
