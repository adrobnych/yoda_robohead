package com.codegemz.elfi.coreapp.helper.state;

import android.content.ContentResolver;
import android.database.Cursor;

import com.codegemz.elfi.apicontracts.StateContract;

/**
 * Created by adrobnych on 6/14/15.
 */
public class StateHelperModule {
    public static int numOfRecords(ContentResolver cr, String[] queryParams) {
        Cursor c = cr.query(
                StateContract.CONTENT_URI, null,
                "name = ?",
                queryParams, null);
        c.moveToFirst();
        int numOfRecords = c.getCount();
        c.close();
        return numOfRecords;
    }
}
