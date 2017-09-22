package com.codegemz.elfi.coreapp.api.content_providers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.codegemz.elfi.apicontracts.FamilyMemberContract;
import com.codegemz.elfi.coreapp.api.FamilyMemberProvider;
import com.codegemz.elfi.model.FamilyMember;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;

/**
 * Created by adrobnych on 8/11/15.
 */
public class FamilyMemberProviderTest extends ProviderTestCase2<FamilyMemberProvider> {
    public static final String TAG = "AlgorithmBundleProvTest";


    MockContentResolver mMockResolver;

    public FamilyMemberProviderTest(Class<FamilyMemberProvider> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public FamilyMemberProviderTest() {
        super(FamilyMemberProvider.class, "com.codegemz.elfi.coreapp.api.FamilyMemberProvider");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(TAG, "setUp: ");

        getHelper().resetAllTables();

        FamilyMemberProvider provider = new FamilyMemberProvider();
        provider.attachInfo(getContext(), null);

        mMockResolver = new MockContentResolver();
        mMockResolver.addProvider(FamilyMemberContract.AUTHORITY, provider);
    }

    private ContentProviderDBHelper getHelper() {
        return new ContentProviderDBHelper(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown:");
    }

    public void testFamilyMemberInsert_insertsAValidRecord() {
        Uri uri = mMockResolver.insert(FamilyMemberContract.CONTENT_URI, getPositiveFixtureFamilyMemberContentValues());
        assertEquals(1L, ContentUris.parseId(uri));
        Cursor cursor = mMockResolver.query(FamilyMemberContract.CONTENT_URI, null,
                null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1,     cursor.getInt(cursor.getColumnIndex(FamilyMemberContract.Columns.ID)));
        assertEquals("mark",     cursor.getString(cursor.getColumnIndex(FamilyMemberContract.Columns.NAME)));
        assertEquals("son",     cursor.getString(cursor.getColumnIndex(FamilyMemberContract.Columns.STATUS)));
        assertEquals(15,     cursor.getInt(cursor.getColumnIndex(FamilyMemberContract.Columns.AGE)));
        assertEquals("uzhgorod gymnasium",     cursor.getString(cursor.getColumnIndex(FamilyMemberContract.Columns.COMPANY)));
        assertEquals("robot maker",     cursor.getString(cursor.getColumnIndex(FamilyMemberContract.Columns.POSITION)));
        assertEquals("j3h4bjh3b53jh4n",     cursor.getString(cursor.getColumnIndex(FamilyMemberContract.Columns.BT_ID)));
    }

    private static final String VALID_NAME    = "mark";
    private static final String VALID_STATUS  = "son";
    private static final int VALID_AGE  = 15;
    private static final String VALID_COMPANY  = "uzhgorod gymnasium";
    private static final String VALID_POSITION  = "robot maker";
    private static final String VALID_BT_ID  = "j3h4bjh3b53jh4n";
    public static ContentValues getPositiveFixtureFamilyMemberContentValues() {
        ContentValues v = new ContentValues(1);
        v.put(FamilyMemberContract.Columns.NAME,        VALID_NAME);
        v.put(FamilyMemberContract.Columns.STATUS,      VALID_STATUS);
        v.put(FamilyMemberContract.Columns.AGE,         VALID_AGE);
        v.put(FamilyMemberContract.Columns.COMPANY,     VALID_COMPANY);
        v.put(FamilyMemberContract.Columns.POSITION,    VALID_POSITION);
        v.put(FamilyMemberContract.Columns.BT_ID,       VALID_BT_ID);
        return v;
    }
}