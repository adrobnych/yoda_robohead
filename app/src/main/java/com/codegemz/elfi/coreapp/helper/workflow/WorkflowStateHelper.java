package com.codegemz.elfi.coreapp.helper.workflow;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.codegemz.elfi.apicontracts.StateContract;
import com.codegemz.elfi.coreapp.helper.state.StateHelperModule;
import com.codegemz.elfi.model.WorkflowType;

/**
 * Created by adrobnych on 6/14/15.
 */
public class WorkflowStateHelper {
    private Context context;
    private ContentResolver contentResolver;

    public WorkflowStateHelper(Context context) {
        this.context = context;
        this.contentResolver = context.getContentResolver();
    }

    public void createOrUpdate(WorkflowType workflowType, String value1, String value2) {
        ContentValues v = new ContentValues();
        v.put(StateContract.Columns.NAME, workflowType.toString());
        v.put(StateContract.Columns.VALUE1, value1);
        v.put(StateContract.Columns.VALUE2, value2);

        if(StateHelperModule.numOfRecords(context.getContentResolver(),
                new String[]{workflowType.toString()}) == 0)
            contentResolver.insert(StateContract.CONTENT_URI, v);
        else
            contentResolver.update(StateContract.CONTENT_URI, v, "name=?",
                    new String[]{workflowType.toString()});
    }

    public void remove(WorkflowType workflowType) {
        contentResolver.delete(StateContract.CONTENT_URI, "name=?", new String[]{workflowType.toString()});
    }

    public void add(WorkflowType workflowType, String option, String istrue) {
        ContentValues v = new ContentValues();
        v.put(StateContract.Columns.NAME, workflowType.toString());
        v.put(StateContract.Columns.VALUE1, option);
        v.put(StateContract.Columns.VALUE2, istrue);
        contentResolver.insert(StateContract.CONTENT_URI, v);
    }


    public void increaseScore() {
        Cursor c = contentResolver.query(
                StateContract.CONTENT_URI, new String[]{
                        StateContract.Columns.VALUE1, StateContract.Columns.VALUE2},
                "name = ?",
                new String[]{WorkflowType.CONVERSATION_QUIZ_SCORE.toString()}, null);
        c.moveToFirst();
        int currentScore = new Integer(c.getString(0)).intValue();
        int numOfSteps = new Integer(c.getString(1)).intValue();
        c.close();
        createOrUpdate(WorkflowType.CONVERSATION_QUIZ_SCORE, ""+(currentScore+1), ""+numOfSteps);
    }

    public String getNameOfTheNextStep() {
        Cursor c = contentResolver.query(
                StateContract.CONTENT_URI, new String[]{
                        StateContract.Columns.VALUE1},
                "name = ?",
                new String[]{WorkflowType.CONVERSATION_QUIZ_NEXT_STEP.toString()}, null);
        c.moveToFirst();
        String result = c.getString(0);
        c.close();
        return result;
    }

    public String getRating() {
        Cursor c = contentResolver.query(
                StateContract.CONTENT_URI, new String[]{
                        StateContract.Columns.VALUE1, StateContract.Columns.VALUE2},
                "name = ?",
                new String[]{WorkflowType.CONVERSATION_QUIZ_SCORE.toString()}, null);
        c.moveToFirst();
        int currentScore = new Integer(c.getString(0)).intValue();
        int numOfSteps = new Integer(c.getString(1)).intValue();
        return ""+currentScore+" of " + numOfSteps;
    }

    public boolean isInWorkflow(WorkflowType workflowType) {
        if(StateHelperModule.numOfRecords(context.getContentResolver(),
                new String[]{workflowType.toString()}) == 0)
            return false;
        else
            return true;
    }


    public String getValue2(WorkflowType workflowType) {
        if(StateHelperModule.numOfRecords(context.getContentResolver(),
                new String[]{workflowType.toString()}) == 0)
            return null;
        else {
            Cursor c = contentResolver.query(
                    StateContract.CONTENT_URI, new String[]{
                            StateContract.Columns.VALUE2},
                    "name = ?",
                    new String[]{workflowType.toString()}, null);
            c.moveToFirst();
            String result = c.getString(0);
            c.close();
            return result;
        }

    }
}
