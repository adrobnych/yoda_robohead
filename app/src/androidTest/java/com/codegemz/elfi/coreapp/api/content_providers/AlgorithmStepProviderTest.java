package com.codegemz.elfi.coreapp.api.content_providers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.codegemz.elfi.apicontracts.AlgorithmStepContract;
import com.codegemz.elfi.coreapp.api.external__algorithms.AlgorithmStepProvider;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;

/**
 * Created by adrobnych on 5/28/15.
 */
public class AlgorithmStepProviderTest extends ProviderTestCase2<AlgorithmStepProvider> {
    public static final String TAG = "AlgorithmStepProvider";


    MockContentResolver mMockResolver;

    public AlgorithmStepProviderTest(Class<AlgorithmStepProvider> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public AlgorithmStepProviderTest() {
        //this.CPTester("com.example.myapp.MyContentProvider",AsanaProvider.class);
        super(AlgorithmStepProvider.class, "com.codegemz.elfi.coreapp.api.external__algorithms.AlgorithmStepProvider");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(TAG, "setUp: ");

        getHelper().resetAllTables();

        AlgorithmStepProvider provider = new AlgorithmStepProvider();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.authority = AlgorithmStepContract.AUTHORITY;
        provider.attachInfo(getContext(), providerInfo);

        mMockResolver = new MockContentResolver();
        mMockResolver.addProvider(AlgorithmStepContract.AUTHORITY, provider);
    }

    private ContentProviderDBHelper getHelper() {
        return new ContentProviderDBHelper(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown:");
    }

    public void testAlgorithmStepInsert_insertsAValidRecord() {
        Uri uri = mMockResolver.insert(AlgorithmStepContract.CONTENT_URI, getPositiveFixtureAlgorithmStepContentValues());
        assertEquals(1L, ContentUris.parseId(uri));
        Cursor cursor = mMockResolver.query(AlgorithmStepContract.CONTENT_URI, null,
                null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1,     cursor.getInt(cursor.getColumnIndex(AlgorithmStepContract.Columns.ID)));
        assertEquals("com.codegemz.elfi.coreapp.api.SAY",     cursor.getString(cursor.getColumnIndex(AlgorithmStepContract.Columns.INTENT)));
    }

    private static final String VALID_INTENT    = "com.codegemz.elfi.coreapp.api.SAY";
    private static final String VALID_ALG_NAME    = "SAY111";
    public static ContentValues getPositiveFixtureAlgorithmStepContentValues() {
        ContentValues v = new ContentValues(1);
        v.put(AlgorithmStepContract.Columns.INTENT,      VALID_INTENT);
        v.put(AlgorithmStepContract.Columns.ALGORITHM_NAME,      VALID_ALG_NAME);
        return v;
    }
}