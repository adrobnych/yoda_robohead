package com.codegemz.elfi.coreapp.api.helpers;

import android.database.Cursor;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.codegemz.elfi.apicontracts.StateContract;
import com.codegemz.elfi.coreapp.api.StateProvider;

import com.codegemz.elfi.coreapp.helper.state.StateHelperModule;
import com.codegemz.elfi.coreapp.helper.workflow.WorkflowStateHelper;
import com.codegemz.elfi.model.WorkflowType;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;

/**
 * Created by adrobnych on 5/24/15.
 */
public class WorflowHelperTest extends ProviderTestCase2<StateProvider> {
    public static final String TAG = "WorkflowHelper";
    private WorkflowStateHelper workflowStateHelper;

    MockContentResolver mMockResolver;

    /**
     * Constructor.
     *
     * @param providerClass     The class name of the provider under test
     * @param providerAuthority The provider's authority string
     */
    public WorflowHelperTest(Class<StateProvider> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public WorflowHelperTest() {
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

        workflowStateHelper = new WorkflowStateHelper(getContext());
    }

    private ContentProviderDBHelper getHelper() {
        return new ContentProviderDBHelper(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown:");
    }

    public void testSetupNewConversation(){

        workflowStateHelper.createOrUpdate(WorkflowType.CONVERSATION_QUIZ_SCORE, "0", "2");

        assertEquals(1, StateHelperModule.numOfRecords(mMockResolver,
                        new String[]{WorkflowType.CONVERSATION_QUIZ_SCORE.toString()}));
    }

    public void testAddConversationDetails(){

        workflowStateHelper.add(WorkflowType.CONVERSATION_QUIZ_OPTIONS, "first quizz option", "true");
        workflowStateHelper.add(WorkflowType.CONVERSATION_QUIZ_OPTIONS, "second quizz option", "false");

        assertEquals(2, StateHelperModule.numOfRecords(mMockResolver,
                new String[]{WorkflowType.CONVERSATION_QUIZ_OPTIONS.toString()}));

        workflowStateHelper.remove(WorkflowType.CONVERSATION_QUIZ_OPTIONS);

        assertEquals(0, StateHelperModule.numOfRecords(mMockResolver,
                new String[]{WorkflowType.CONVERSATION_QUIZ_OPTIONS.toString()}));
    }


    public void testIncreaseScore(){
        workflowStateHelper.createOrUpdate(WorkflowType.CONVERSATION_QUIZ_SCORE, "0", "2");
        workflowStateHelper.increaseScore();

        Cursor c = mMockResolver.query(
                StateContract.CONTENT_URI, new String[]{StateContract.Columns.VALUE1},
                "name = ?",
                new String[]{WorkflowType.CONVERSATION_QUIZ_SCORE.toString()}, null);
        c.moveToFirst();
        int currentScore = new Integer(c.getString(0)).intValue();
        c.close();

        assertEquals(1, currentScore);
    }

}
