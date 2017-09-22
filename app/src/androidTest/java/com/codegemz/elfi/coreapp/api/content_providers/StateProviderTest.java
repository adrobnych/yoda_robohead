package com.codegemz.elfi.coreapp.api.content_providers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.codegemz.elfi.apicontracts.StateContract;
import com.codegemz.elfi.coreapp.api.StateProvider;
import com.codegemz.elfi.coreapp.helper.voice_recognition.TalkStateHelper;
import com.codegemz.elfi.model.EmojiManager;
import com.codegemz.elfi.apicontracts.EmojiType;
import com.codegemz.elfi.model.TalkStateType;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;

/**
 * Created by adrobnych on 5/24/15.
 */
public class StateProviderTest extends ProviderTestCase2<StateProvider> {
    public static final String TAG = "StateProvider";


    MockContentResolver mMockResolver;

    /**
     * Constructor.
     *
     * @param providerClass     The class name of the provider under test
     * @param providerAuthority The provider's authority string
     */
    public StateProviderTest(Class<StateProvider> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public StateProviderTest() {
        //this.CPTester("com.example.myapp.MyContentProvider",AsanaProvider.class);
        super(StateProvider.class,"com.codegemz.elfi.coreapp.api.StateProvider");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(TAG, "setUp: ");

        getHelper().resetAllTables();

        StateProvider provider = new StateProvider();
        provider.attachInfo(getContext(), null);

        mMockResolver = new MockContentResolver();
        mMockResolver.addProvider(StateContract.AUTHORITY, provider);
    }

    private ContentProviderDBHelper getHelper() {
        return new ContentProviderDBHelper(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown:");
    }

    public void testStatesInsert__inserts_a_valid_record() {
        Uri uri = mMockResolver.insert(StateContract.CONTENT_URI, getPositiveFixtureEmotionStateContentValues());
        assertEquals(1L, ContentUris.parseId(uri));
    }

    public void testStatesInsert__cursor_contains_valid_data() {
        mMockResolver.insert(StateContract.CONTENT_URI, getPositiveFixtureEmotionStateContentValues());
        Cursor cursor = mMockResolver.query(StateContract.CONTENT_URI, null, null, new String[] {}, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals("emotion",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.NAME)));
        assertEquals("Happy",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE1)));
        assertEquals("1",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE2)));
    }

    public void testStatesQueryForEmotion__cursor_contains_valid_data() {
        String[] projection = { StateContract.Columns.ID,
                                StateContract.Columns.NAME,
                                StateContract.Columns.VALUE1,
                                StateContract.Columns.VALUE2};
        mMockResolver.insert(StateContract.CONTENT_URI, getPositiveFixtureEmotionStateContentValues());
        Cursor cursor = mMockResolver.query(    StateContract.CONTENT_URI, projection,
                                                "name = ?",
                                                new String[]{"emotion"}, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1,     cursor.getInt(cursor.getColumnIndex(StateContract.Columns.ID)));
        assertEquals("emotion",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.NAME)));
        assertEquals("Happy",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE1)));
        assertEquals("1",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE2)));
    }

    public void testStatesQueryForTalk__cursor_contains_valid_data() {
        mMockResolver.insert(StateContract.CONTENT_URI, getPositiveFixtureTalkStateContentValues());
        Cursor cursor = mMockResolver.query(    StateContract.CONTENT_URI, null,
                "name = ?",
                new String[]{"talk"}, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1,     cursor.getInt(cursor.getColumnIndex(StateContract.Columns.ID)));
        assertEquals("talk",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.NAME)));
        assertEquals("Active",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE1)));
        assertEquals(VALID_TALK_STATE_VALUE2,     cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE2)));
    }

    public void testTalkStateHelperShouldCreateOrUpdateTalkRecord(){
        //test that there is no talk records
        Cursor cursor = mMockResolver.query(    StateContract.CONTENT_URI, null,
                "name = ?",
                new String[]{"talk"}, null);
        assertNotNull(cursor);
        assertEquals(0, cursor.getCount());

        //add new record
        TalkStateHelper talkStateHelper = new TalkStateHelper(getContext());
        talkStateHelper.setTalkSate(TalkStateType.Inactive);

        //test new talk record
        cursor = mMockResolver.query(    StateContract.CONTENT_URI, null,
                "name = ?",
                new String[]{"talk"}, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1,     cursor.getInt(cursor.getColumnIndex(StateContract.Columns.ID)));
        assertEquals("talk",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.NAME)));
        assertEquals("Inactive",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE1)));
        assertNotSame("",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE2)));

        //test update talk record

        talkStateHelper.setTalkSate(TalkStateType.Active);

        cursor = mMockResolver.query(    StateContract.CONTENT_URI, null,
                "name = ?",
                new String[]{"talk"}, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1,     cursor.getInt(cursor.getColumnIndex(StateContract.Columns.ID)));
        assertEquals("talk",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.NAME)));
        assertEquals("Active",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE1)));
        assertNotSame("",     cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE2)));
    }


    private static final String VALID_EMOTION_STATE_NAME    = "emotion";
    private static final String VALID_EMOTION_STATE_VALUE1  = EmojiType.Happy.toString();
    private static final String VALID_EMOTION_STATE_VALUE2  = "" + (new EmojiManager()).getMaxEmotionLevel(EmojiType.Happy);
    public static ContentValues getPositiveFixtureEmotionStateContentValues() {
        ContentValues v = new ContentValues(1);
        v.put(StateContract.Columns.NAME,       VALID_EMOTION_STATE_NAME);
        v.put(StateContract.Columns.VALUE1,     VALID_EMOTION_STATE_VALUE1);
        v.put(StateContract.Columns.VALUE2,     VALID_EMOTION_STATE_VALUE2);
        return v;
    }

    private static final String VALID_TALK_STATE_NAME    = "talk";
    private static final String VALID_TALK_STATE_VALUE1  = TalkStateType.Active.toString();
    //time since last detection of human face
    private static final String VALID_TALK_STATE_VALUE2  = "" + System.currentTimeMillis();
    public static ContentValues getPositiveFixtureTalkStateContentValues() {
        ContentValues v = new ContentValues();
        v.put(StateContract.Columns.NAME,       VALID_TALK_STATE_NAME);
        v.put(StateContract.Columns.VALUE1,     VALID_TALK_STATE_VALUE1);
        v.put(StateContract.Columns.VALUE2,     VALID_TALK_STATE_VALUE2);
        return v;
    }

}
