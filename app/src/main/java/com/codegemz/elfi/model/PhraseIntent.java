package com.codegemz.elfi.model;

import android.provider.BaseColumns;

import com.codegemz.elfi.apicontracts.PhraseIntentContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation;

/**
 * Created by adrobnych on 5/28/15.
 */
@DatabaseTable(tableName = PhraseIntentContract.TABLE_NAME)
@AdditionalAnnotation.DefaultContentUri(authority = PhraseIntentContract.AUTHORITY, path = PhraseIntentContract.CONTENT_URI_PATH)
@AdditionalAnnotation.DefaultContentMimeTypeVnd(name = PhraseIntentContract.MIMETYPE_NAME, type = PhraseIntentContract.MIMETYPE_TYPE)
public class PhraseIntent {

    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @AdditionalAnnotation.DefaultSortOrder
    private int id;

    @DatabaseField
    private String spoken_answer;

    @DatabaseField
    private String intent_broadcast;

    @DatabaseField
    private String intent_json_extras;

    @DatabaseField
    private String input_context;

    @DatabaseField
    private String output_context;

    public PhraseIntent() {
        // ORMLite needs a no-arg constructor
    }


    public PhraseIntent(String spoken_answer, String intent_broadcast, String intent_json_extras, String input_context, String output_context) {
        this.id = 0;
        this.spoken_answer = spoken_answer;
        this.intent_broadcast = intent_broadcast;
        this.intent_json_extras = intent_json_extras;
        this.input_context = input_context;
        this.output_context = output_context;
    }

    public String getSpoken_answer() {
        return spoken_answer;
    }

    public String getIntent_broadcast() {
        return intent_broadcast;
    }

    public String getIntent_json_extras() {
        return intent_json_extras;
    }

    public String getInput_context() {
        return input_context;
    }

    public String getOutput_context() {
        return output_context;
    }
}

