package com.codegemz.elfi.coreapp.api.content_providers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.codegemz.elfi.apicontracts.AlgorithmBundleContract;
import com.codegemz.elfi.coreapp.api.external__algorithms.AlgorithmBundleProvider;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;

/**
 * Created by adrobnych on 5/30/15.
 */
public class AlgorithmBundleProviderTest extends ProviderTestCase2<AlgorithmBundleProvider> {
    public static final String TAG = "AlgorithmBundleProvTest";


    MockContentResolver mMockResolver;

    public AlgorithmBundleProviderTest(Class<AlgorithmBundleProvider> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public AlgorithmBundleProviderTest() {
        //this.CPTester("com.example.myapp.MyContentProvider",AsanaProvider.class);
        super(AlgorithmBundleProvider.class, "com.codegemz.elfi.coreapp.api.external__algorithms.AlgorithmBundleProvider");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(TAG, "setUp: ");

        getHelper().resetAllTables();

        AlgorithmBundleProvider provider = new AlgorithmBundleProvider();
        provider.attachInfo(getContext(), null);

        mMockResolver = new MockContentResolver();
        mMockResolver.addProvider(AlgorithmBundleContract.AUTHORITY, provider);
    }

    private ContentProviderDBHelper getHelper() {
        return new ContentProviderDBHelper(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown:");
    }

    public void testAlgorithmBundleInsert_insertsAValidRecord() {
        Uri uri = mMockResolver.insert(AlgorithmBundleContract.CONTENT_URI, getPositiveFixtureAlgorithmBundleContentValues());
        assertEquals(1L, ContentUris.parseId(uri));
        Cursor cursor = mMockResolver.query(AlgorithmBundleContract.CONTENT_URI, null,
                null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1,     cursor.getInt(cursor.getColumnIndex(AlgorithmBundleContract.Columns.ID)));
        assertEquals("com.codegemz.DEFAULT_BEHAVIOR_BUNDLE",     cursor.getString(cursor.getColumnIndex(AlgorithmBundleContract.Columns.NAME)));
        assertEquals("http://androcommerce.com",     cursor.getString(cursor.getColumnIndex(AlgorithmBundleContract.Columns.URL)));
    }

    private static final String VALID_NAME    = "com.codegemz.DEFAULT_BEHAVIOR_BUNDLE";
    private static final String VALID_URL  = "http://androcommerce.com";
    public static ContentValues getPositiveFixtureAlgorithmBundleContentValues() {
        ContentValues v = new ContentValues(1);
        v.put(AlgorithmBundleContract.Columns.NAME,      VALID_NAME);
        v.put(AlgorithmBundleContract.Columns.URL,  VALID_URL);
        return v;
    }
}