package com.codegemz.elfi.apimanagers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.test.IsolatedContext;

import com.codegemz.elfi.apicontracts.PhraseIntentContract;
import com.codegemz.elfi.apicontracts.StateContract;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by adrobnych on 4/2/16.
 */
public class PhraseIntentManager {

    private Context context;
    private static final String VALID_STATE_NAME = "question_and_answer";

    public PhraseIntentManager(Context context) {
        this.context = context;
    }

    public int createPhraseIntent(PhraseIntentDTO piDTO){

        Uri piUri = context.getContentResolver().insert(PhraseIntentContract.CONTENT_URI, prepareContentValues(piDTO));

        int resultPhraseIntentID = (int)ContentUris.parseId(piUri);

        StateManager stateManager = new StateManager(context);

        List<StateDTO> sDTOs = new LinkedList<>();
        for(String pattern : piDTO.getPattenrns()) {
            StateDTO sDTO = new StateDTO(VALID_STATE_NAME, pattern, "" + resultPhraseIntentID);
            stateManager.createState(sDTO);
        }
        return resultPhraseIntentID;
    }

    public ContentValues prepareContentValues(PhraseIntentDTO piDTO) {
        ContentValues v = new ContentValues(1);
        v.put(PhraseIntentContract.Columns.SPOKEN_ANSWER, piDTO.getSpoken_answer());
        v.put(PhraseIntentContract.Columns.INPUT_CONTEXT, piDTO.getInput_context());
        v.put(PhraseIntentContract.Columns.OUTPUT_CONTEXT, piDTO.getOutput_context());
        v.put(PhraseIntentContract.Columns.INTENT_BROADCAST, piDTO.getIntent_broadcast());
        v.put(PhraseIntentContract.Columns.INTENT_JSON_EXTRAS, piDTO.getIntent_json_extras());
        return v;
    }

}