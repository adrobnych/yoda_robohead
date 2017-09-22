package com.codegemz.elfi.coreapp.helper.family_member_helper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.codegemz.elfi.apicontracts.StateContract;

/**
 * Created by adrobnych on 5/26/15.
 */
public class NewFamilyMemberStateHelper {

    Context context;
    ContentResolver cr;

    public NewFamilyMemberStateHelper(Context context){
        this.context = context;
        cr = context.getContentResolver();
    }

    private Cursor getNewFamilyMemberCursor(){
        Cursor c = cr.query(
                StateContract.CONTENT_URI, null,
                "name = ?",
                new String[]{"new_family_member"}, null);
        c.moveToFirst();
        return  c;
    }

    public boolean isItNewFamilyMemberState() {
        boolean result = true;
        Cursor c = getNewFamilyMemberCursor();
        if(c.getCount() != 0)
            result = false;
        c.close();
        return result;
    }

    public void createDefaultNewFamilyMemberStateWithFMID(String fmId){
        ContentValues v = new ContentValues();
        v.put(StateContract.Columns.NAME,       "new_family_member");
        v.put(StateContract.Columns.VALUE1,     "new_family_member_conversation");
        v.put(StateContract.Columns.VALUE2,     fmId);
        cr.insert(StateContract.CONTENT_URI, v);
    }

    public long getFamilyMemberIdFromStateCP() {
        long result = -1;
        Cursor c = getNewFamilyMemberCursor();
        if(c.getCount() != 0) {
            c.moveToFirst();
            result = new Long(c.getString(c.getColumnIndex(StateContract.Columns.VALUE2))).longValue();
        }
        c.close();
        return result;
    }

    public void finishFamilyMemberState() {
        cr.delete(
                StateContract.CONTENT_URI,
                "name = ?",
                new String[]{"new_family_member"});
    }
}
