package com.codegemz.elfi.coreapp.api.api_managers;

import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;

import com.codegemz.elfi.apicontracts.StateContract;
import com.codegemz.elfi.apimanagers.StateDTO;
import com.codegemz.elfi.apimanagers.StateManager;
import com.codegemz.elfi.coreapp.BrainActivity;
import com.codegemz.elfi.model.State;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;

/**
 * Created by adrobnych on 4/4/16.
 */
public class StateManagerTest extends ActivityInstrumentationTestCase2<BrainActivity> {
    public static final String TAG = "PhraseIntentManagerTest";

    public StateManagerTest() {
        super(BrainActivity.class);
    }

    private static final String VALID_STATE2_NAME = "question_and_answer";
    private static final String VALID_STATE2_VALUE1 = "say me something interesting about stars";
    private static final String VALID_STATE2_VALUE2 = "1";

    private StateManager sManager;
    private BrainActivity brainActivity;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        brainActivity = getActivity();
        sManager = new StateManager(brainActivity);

        (new ContentProviderDBHelper(brainActivity)).resetAllTables();
    }


    public void testStateDelete_insertsAValidRecord() {

       StateDTO sDTO = new StateDTO(
               VALID_STATE2_NAME,
               VALID_STATE2_VALUE1,
               VALID_STATE2_VALUE2
       );

        int new_id = sManager.createState(sDTO);

        assertEquals(1, new_id);

        Cursor cursor = brainActivity.getContentResolver().query(StateContract.CONTENT_URI, null,
                null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(1, cursor.getInt(cursor.getColumnIndex(StateContract.Columns.ID)));
        assertEquals(VALID_STATE2_NAME, cursor.getString(cursor.getColumnIndex(StateContract.Columns.NAME)));
        assertEquals(VALID_STATE2_VALUE1, cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE1)));
        assertEquals(VALID_STATE2_VALUE2, cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE2)));

        sManager.deleteStateByName(VALID_STATE2_NAME);

        cursor = brainActivity.getContentResolver().query(StateContract.CONTENT_URI, null,
                null, null, null);
        assertNotNull(cursor);
        assertEquals(0, cursor.getCount());

    }
}
