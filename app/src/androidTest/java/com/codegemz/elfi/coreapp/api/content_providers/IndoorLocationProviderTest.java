package com.codegemz.elfi.coreapp.api.content_providers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.codegemz.elfi.apicontracts.IndoorLocationContract;
import com.codegemz.elfi.coreapp.api.IndoorLocationProvider;
import com.codegemz.elfi.model.IndoorLocation;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;

/**
 * Created by adrobnych on 5/28/15.
 */
public class IndoorLocationProviderTest extends ProviderTestCase2<IndoorLocationProvider> {
    public static final String TAG = "AlgorithmProvider";


    MockContentResolver mMockResolver;

    public IndoorLocationProviderTest(Class<IndoorLocationProvider> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public IndoorLocationProviderTest() {
        super(IndoorLocationProvider.class, "com.codegemz.elfi.coreapp.api.IndoorLocationProvider");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(TAG, "setUp: ");

        getHelper().resetAllTables();

        IndoorLocationProvider provider = new IndoorLocationProvider();
        provider.attachInfo(getContext(), null);

        mMockResolver = new MockContentResolver();
        mMockResolver.addProvider(IndoorLocationContract.AUTHORITY, provider);
    }

    private ContentProviderDBHelper getHelper() {
        return new ContentProviderDBHelper(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown:");
    }

    public void testAlgorithmInsert_insertsAValidRecord() {
        Uri uri = mMockResolver.insert(IndoorLocationContract.CONTENT_URI, getPositiveFixtureAlgorithmContentValues());
        assertEquals(1L, ContentUris.parseId(uri));
        Cursor cursor = mMockResolver.query(IndoorLocationContract.CONTENT_URI, null,
                null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1,     cursor.getInt(cursor.getColumnIndex(IndoorLocationContract.Columns.ID)));
        assertEquals("kitchen",     cursor.getString(cursor.getColumnIndex(IndoorLocationContract.Columns.NAME)));
        assertEquals("wifi_111",     cursor.getString(cursor.getColumnIndex(IndoorLocationContract.Columns.WIFI_SSID)));
        assertEquals((float)0.5,     cursor.getFloat(cursor.getColumnIndex(IndoorLocationContract.Columns.WIFI_CORRECTION_COEFFICIENT)));

    }

    private static final String VALID_NAME  = "kitchen";
    private static final String VALID_WIFI  = "wifi_111";
    private static final Float VALID_WIFI_CORRECTION = (float)0.5;
    public static ContentValues getPositiveFixtureAlgorithmContentValues() {
        ContentValues v = new ContentValues(1);
        v.put(IndoorLocationContract.Columns.NAME,  VALID_NAME);
        v.put(IndoorLocationContract.Columns.WIFI_SSID,  VALID_WIFI);
        v.put(IndoorLocationContract.Columns.WIFI_CORRECTION_COEFFICIENT,  VALID_WIFI_CORRECTION);
        return v;
    }
}