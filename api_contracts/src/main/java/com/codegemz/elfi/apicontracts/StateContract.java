package com.codegemz.elfi.apicontracts;

/**
 * Created by adrobnych on 5/24/15.
 */
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class StateContract implements BaseColumns {

    private StateContract() {
        // Utility constructor
    }

    public static final String TABLE_NAME = "State";

    public static final String AUTHORITY = "com.codegemz.elfi.coreapp.api.state_provider";

    public static final String CONTENT_URI_PATH = "states";

    public static final String MIMETYPE_TYPE = "states";
    public static final String MIMETYPE_NAME = "com.codegemz.elfi.coreapp.api";

    public static final int CONTENT_URI_PATTERN_MANY = 1;
    public static final int CONTENT_URI_PATTERN_ONE = 2;

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build();

    public static class Columns{
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String VALUE1 = "value1";
        public static final String VALUE2 = "value2";
    }

}
