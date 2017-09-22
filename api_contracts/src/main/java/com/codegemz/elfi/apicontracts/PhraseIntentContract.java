package com.codegemz.elfi.apicontracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by adrobnych on 5/28/15.
 */
public final class PhraseIntentContract implements BaseColumns {

    private PhraseIntentContract() {
        // Utility constructor
    }

    public static final String TABLE_NAME = "PhraseIntent";

    //public static final String AUTHORITY = "com.codegemz.elfi.coreapp.api";
    public static final String AUTHORITY = "com.codegemz.elfi.coreapp.api.phrase_intent_provider";

    public static final String CONTENT_URI_PATH = "phrase_intents";

    public static final String MIMETYPE_TYPE = "phrase_intents";
    public static final String MIMETYPE_NAME = "com.codegemz.elfi.coreapp.api";

    public static final int CONTENT_URI_PATTERN_MANY = 1;
    public static final int CONTENT_URI_PATTERN_ONE = 2;

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build();

    public static class Columns{
        public static final String ID = "_id";
        public static final String SPOKEN_ANSWER = "spoken_answer";
        public static final String INTENT_BROADCAST = "intent_broadcast";
        public static final String INTENT_JSON_EXTRAS = "intent_json_extras";
        public static final String INPUT_CONTEXT = "input_context";
        public static final String OUTPUT_CONTEXT = "output_context";

    }

}
