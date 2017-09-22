package com.codegemz.elfi.coreapp.api.content_providers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.codegemz.elfi.apicontracts.PhraseIntentContract;
import com.codegemz.elfi.coreapp.api.PhraseIntentProvider;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;

/**
 * Created by adrobnych on 5/28/15.
 */
public class PhraseIntentProviderTest extends ProviderTestCase2<PhraseIntentProvider> {
    public static final String TAG = "PhraseIntentProvider";


    MockContentResolver mMockResolver;

    public PhraseIntentProviderTest(Class<PhraseIntentProvider> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public PhraseIntentProviderTest() {
        super(PhraseIntentProvider.class, "com.codegemz.elfi.coreapp.api.PhraseIntentProvider");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(TAG, "setUp: ");

        getHelper().resetAllTables();

        PhraseIntentProvider provider = new PhraseIntentProvider();
        provider.attachInfo(getContext(), null);

        mMockResolver = new MockContentResolver();
        mMockResolver.addProvider(PhraseIntentContract.AUTHORITY, provider);
    }

    private ContentProviderDBHelper getHelper() {
        return new ContentProviderDBHelper(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown:");
    }

    public void testPhraseInsert_insertsAValidRecord() {
        Uri uri = mMockResolver.insert(PhraseIntentContract.CONTENT_URI, getPositiveFixturePhraseContentValues());
        assertEquals(1L, ContentUris.parseId(uri));
        Cursor cursor = mMockResolver.query(PhraseIntentContract.CONTENT_URI, null,
                null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1, cursor.getInt(cursor.getColumnIndex(PhraseIntentContract.Columns.ID)));
        assertEquals("andromeda",     cursor.getString(cursor.getColumnIndex(PhraseIntentContract.Columns.SPOKEN_ANSWER)));
        assertEquals("astrophysics",     cursor.getString(cursor.getColumnIndex(PhraseIntentContract.Columns.INPUT_CONTEXT)));
        assertEquals("astrophysics, supernova_stars",     cursor.getString(cursor.getColumnIndex(PhraseIntentContract.Columns.OUTPUT_CONTEXT)));
        assertEquals("edu.externalapp.on_mention",     cursor.getString(cursor.getColumnIndex(PhraseIntentContract.Columns.INTENT_BROADCAST)));
        assertEquals("{\"radius\" : 10}",     cursor.getString(cursor.getColumnIndex(PhraseIntentContract.Columns.INTENT_JSON_EXTRAS)));
    }

    private static final String VALID_SPOKEN_ANSWER    = "andromeda";
    private static final String VALID_INPUT_CONTEXT  = "astrophysics";
    private static final String VALID_OUTPUT_CONTEXT  = "astrophysics, supernova_stars";
    private static final String VALID_INTENT_BROADCAST  = "edu.externalapp.on_mention";
    private static final String VALID_INTENT_JSON_EXTRAS  = "{\"radius\" : 10}";
    public static ContentValues getPositiveFixturePhraseContentValues() {
        ContentValues v = new ContentValues(1);
        v.put(PhraseIntentContract.Columns.SPOKEN_ANSWER, VALID_SPOKEN_ANSWER);
        v.put(PhraseIntentContract.Columns.INPUT_CONTEXT, VALID_INPUT_CONTEXT);
        v.put(PhraseIntentContract.Columns.OUTPUT_CONTEXT, VALID_OUTPUT_CONTEXT);
        v.put(PhraseIntentContract.Columns.INTENT_BROADCAST, VALID_INTENT_BROADCAST);
        v.put(PhraseIntentContract.Columns.INTENT_JSON_EXTRAS, VALID_INTENT_JSON_EXTRAS);
        return v;
    }
}