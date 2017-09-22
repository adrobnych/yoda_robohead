package com.codegemz.elfi.apicontracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by adrobnych on 5/28/15.
 */
public final class AlgorithmStepContract implements BaseColumns {

    private AlgorithmStepContract() {
        // Utility constructor
    }

    public static final String TABLE_NAME = "AlgorithmStep";

    public static final String AUTHORITY = "com.codegemz.elfi.coreapp.api.algorithm_step_provider";

    public static final String CONTENT_URI_PATH = "algorithm_steps";

    public static final String MIMETYPE_TYPE = "algorithm_steps";
    public static final String MIMETYPE_NAME = "com.codegemz.elfi.coreapp.api";

    public static final int CONTENT_URI_PATTERN_MANY = 1;
    public static final int CONTENT_URI_PATTERN_ONE = 2;

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build();

    public static class Columns{
        public static final String ID = "_id";
        public static final String PRIORITY = "priority";
        public static final String INTENT = "intent";
        public static final String VALUE1 = "value1";
        public static final String VALUE2 = "value2";
        public static final String ALGORITHM_NAME = "algorithm_name";
    }

}
