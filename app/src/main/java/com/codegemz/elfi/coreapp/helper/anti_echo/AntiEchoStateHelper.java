package com.codegemz.elfi.coreapp.helper.anti_echo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.codegemz.elfi.apicontracts.StateContract;

/**
 * Created by adrobnych on 6/9/15.
 */
public class AntiEchoStateHelper {
    private Context context;
    private ContentResolver cr;

    public AntiEchoStateHelper(Context context){
        this.context = context;
        cr = context.getContentResolver();
    }

    public boolean isEcho(String lastSentence) {
        Log.e(lastSentence+ ":check for pattern:", getCurrentEcho());
        if(lastSentence.matches(getCurrentEcho()))
            return true;
        else
            return false;
    }


    private int numOfEchoRecordsForString(String lastSentence) {

        Cursor c = cr.query(
                StateContract.CONTENT_URI, null,
                "name = ? and value1 = ?",
                new String[]{"echo", lastSentence}, null);
        c.moveToFirst();
        int numOfRecords = c.getCount();
        c.close();
        return numOfRecords;
    }

    private int numOfEchoRecords() {

        Cursor c = cr.query(
                StateContract.CONTENT_URI, null,
                "name = ?",
                new String[]{"echo"}, null);
        c.moveToFirst();
        int numOfRecords = c.getCount();
        c.close();
        return numOfRecords;
    }

    private String getCurrentEcho(){
        String result = null;
        Cursor c = cr.query(
                StateContract.CONTENT_URI, new String[]{StateContract.Columns.VALUE1},
                "name = ?",
                new String[]{"echo"}, null);
        c.moveToFirst();
        int numOfRecords = c.getCount();
        if(numOfRecords > 0)
            result = c.getString(0);
        c.close();
        return result;
    }

    public void setEchoState(String lastSentence) {

            ContentValues v = new ContentValues();
            v.put(StateContract.Columns.VALUE1,     lastSentence);
            cr.update(StateContract.CONTENT_URI, v, "name = ?", new String[]{"echo"});
    }



    public void unsetEchoState() {
        setEchoState("");
    }

    public void createDefaultEchoSet(){
        if(numOfEchoRecords() == 0) {
            ContentValues v = new ContentValues();
            v.put(StateContract.Columns.NAME, "echo");
            v.put(StateContract.Columns.VALUE1, "");
            v.put(StateContract.Columns.VALUE2, "");
            cr.insert(StateContract.CONTENT_URI, v);
        }
    }


}
