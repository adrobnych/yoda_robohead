package com.codegemz.elfi.coreapp.api.content_providers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;


import com.codegemz.elfi.apicontracts.AlgorithmContract;
import com.codegemz.elfi.apicontracts.AlgorithmGroup;
import com.codegemz.elfi.coreapp.api.external__algorithms.AlgorithmProvider;
import com.codegemz.elfi.apicontracts.AlgorithmType;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;

/**
 * Created by adrobnych on 5/28/15.
 */
public class AlgorithmProviderTest extends ProviderTestCase2<AlgorithmProvider> {
    public static final String TAG = "AlgorithmProvider";


    MockContentResolver mMockResolver;

    public AlgorithmProviderTest(Class<AlgorithmProvider> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public AlgorithmProviderTest() {
        super(AlgorithmProvider.class, "com.codegemz.elfi.coreapp.api.external__algorithms.AlgorithmProvider");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(TAG, "setUp: ");

        getHelper().resetAllTables();

        AlgorithmProvider provider = new AlgorithmProvider();
        provider.attachInfo(getContext(), null);

        mMockResolver = new MockContentResolver();
        mMockResolver.addProvider(AlgorithmContract.AUTHORITY, provider);
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
        Uri uri = mMockResolver.insert(AlgorithmContract.CONTENT_URI, getPositiveFixtureAlgorithmContentValues());
        assertEquals(1L, ContentUris.parseId(uri));
        Cursor cursor = mMockResolver.query(AlgorithmContract.CONTENT_URI, null,
                null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1,     cursor.getInt(cursor.getColumnIndex(AlgorithmContract.Columns.ID)));
        assertEquals("say_hello",     cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.NAME)));
        assertEquals("Parallel",     cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.TYPE)));
        assertEquals("com.codegemz.elfi.coreapp.DEFAULT_BEHAVIOR",     cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.ALGORITHM_BUNDLE_NAME)));
        assertEquals("General",     cursor.getString(cursor.getColumnIndex(AlgorithmContract.Columns.GROUP)));
    }

    private static final String VALID_NAME  = "say_hello";
    private static final String VALID_TYPE  = AlgorithmType.Parallel.toString();
    private static final String VALID_AB_NAME = "com.codegemz.elfi.coreapp.DEFAULT_BEHAVIOR";
    public static ContentValues getPositiveFixtureAlgorithmContentValues() {
        ContentValues v = new ContentValues(1);
        v.put(AlgorithmContract.Columns.NAME,  VALID_NAME);
        v.put(AlgorithmContract.Columns.TYPE,  VALID_TYPE);
        v.put(AlgorithmContract.Columns.GROUP, AlgorithmGroup.General.toString());
        v.put(AlgorithmContract.Columns.ALGORITHM_BUNDLE_NAME,  VALID_AB_NAME);
        return v;
    }
}