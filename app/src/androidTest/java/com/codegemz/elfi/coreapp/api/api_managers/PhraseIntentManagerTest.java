package com.codegemz.elfi.coreapp.api.api_managers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.codegemz.elfi.apicontracts.PhraseIntentContract;
import com.codegemz.elfi.apicontracts.StateContract;
import com.codegemz.elfi.apimanagers.AlgorithmManager;
import com.codegemz.elfi.apimanagers.AlgorithmStepManager;
import com.codegemz.elfi.apimanagers.IndoorLocationManager;
import com.codegemz.elfi.apimanagers.PhraseIntentDTO;
import com.codegemz.elfi.apimanagers.PhraseIntentManager;
import com.codegemz.elfi.coreapp.BrainActivity;
import com.codegemz.elfi.coreapp.api.PhraseIntentProvider;
import com.codegemz.elfi.coreapp.api.StateProvider;
import com.codegemz.elfi.model.State;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;

import java.util.LinkedList;

/**
 * Created by adrobnych on 5/28/15.
 */
public class PhraseIntentManagerTest extends ActivityInstrumentationTestCase2<BrainActivity> {
    public static final String TAG = "PhraseIntentManagerTest";

    public PhraseIntentManagerTest() {
        super(BrainActivity.class);
    }

    private static final String VALID_PATTERN1    = "tell me about stars";
    private static final String VALID_PATTERN2    = "say me something interesting about stars";
    private static final String VALID_SPOKEN_ANSWER    = "andromeda1";
    private static final String VALID_INPUT_CONTEXT  = "astrophysics1";
    private static final String VALID_OUTPUT_CONTEXT  = "astrophysics, supernova_stars1";
    private static final String VALID_INTENT_BROADCAST  = "edu.externalapp.on_mention1";
    private static final String VALID_INTENT_JSON_EXTRAS  = "{\"radius\" : 101}";

    private static final String VALID_STATE1_NAME = "question_and_answer";
    private static final String VALID_STATE1_VALUE1 = "tell me about stars";
    private static final String VALID_STATE1_VALUE2 = "1"; // id of PhraseIntent

    private static final String VALID_STATE2_NAME = "question_and_answer";
    private static final String VALID_STATE2_VALUE1 = "say me something interesting about stars";
    private static final String VALID_STATE2_VALUE2 = "1";

    private PhraseIntentManager piManager;
    private BrainActivity brainActivity;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        brainActivity = getActivity();
        piManager = new PhraseIntentManager(brainActivity);

        (new ContentProviderDBHelper(brainActivity)).resetAllTables();
    }


    public void testPhraseIntentCreation_insertsAValidRecord() {

        PhraseIntentDTO piDTO = new PhraseIntentDTO(
                new LinkedList<String>(){{
                    add(VALID_PATTERN1);
                    add(VALID_PATTERN2);
                }},
                VALID_SPOKEN_ANSWER,
                VALID_INTENT_BROADCAST,
                VALID_INTENT_JSON_EXTRAS,
                VALID_INPUT_CONTEXT,
                VALID_OUTPUT_CONTEXT);

        int new_id = piManager.createPhraseIntent(piDTO);

        assertEquals(1, new_id);

        Cursor cursor = brainActivity.getContentResolver().query(PhraseIntentContract.CONTENT_URI, null,
                null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1, cursor.getInt(cursor.getColumnIndex(PhraseIntentContract.Columns.ID)));
        assertEquals(VALID_SPOKEN_ANSWER, cursor.getString(cursor.getColumnIndex(PhraseIntentContract.Columns.SPOKEN_ANSWER)));
        assertEquals(VALID_INPUT_CONTEXT, cursor.getString(cursor.getColumnIndex(PhraseIntentContract.Columns.INPUT_CONTEXT)));
        assertEquals(VALID_OUTPUT_CONTEXT, cursor.getString(cursor.getColumnIndex(PhraseIntentContract.Columns.OUTPUT_CONTEXT)));
        assertEquals(VALID_INTENT_BROADCAST, cursor.getString(cursor.getColumnIndex(PhraseIntentContract.Columns.INTENT_BROADCAST)));
        assertEquals(VALID_INTENT_JSON_EXTRAS,     cursor.getString(cursor.getColumnIndex(PhraseIntentContract.Columns.INTENT_JSON_EXTRAS)));

        cursor = brainActivity.getContentResolver().query(StateContract.CONTENT_URI, null,
                null, null, null);
        assertNotNull(cursor);
        assertEquals(2, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1, cursor.getInt(cursor.getColumnIndex(PhraseIntentContract.Columns.ID)));
        assertEquals(VALID_STATE1_NAME, cursor.getString(cursor.getColumnIndex(StateContract.Columns.NAME)));
        assertEquals(VALID_STATE1_VALUE1, cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE1)));
        assertEquals(VALID_STATE1_VALUE2, cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE2)));

        assertTrue(cursor.moveToNext());
        assertEquals(2, cursor.getInt(cursor.getColumnIndex(PhraseIntentContract.Columns.ID)));
        assertEquals(VALID_STATE2_NAME, cursor.getString(cursor.getColumnIndex(StateContract.Columns.NAME)));
        assertEquals(VALID_STATE2_VALUE1,     cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE1)));
        assertEquals(VALID_STATE2_VALUE2,     cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE2)));
    }



}