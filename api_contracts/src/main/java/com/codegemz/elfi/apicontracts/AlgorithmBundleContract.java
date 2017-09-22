package com.codegemz.elfi.apicontracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by adrobnych on 5/30/15.
 */
public class AlgorithmBundleContract implements BaseColumns {

    private AlgorithmBundleContract() {
        // Utility constructor
    }

    public static final String TABLE_NAME = "AlgorithmBundle";

    public static final String AUTHORITY = "com.codegemz.elfi.coreapp.api.algorithm_bundle_provider";

    public static final String CONTENT_URI_PATH = "algorithm_bundles";

    public static final String MIMETYPE_TYPE = "algorithm_bundles";
    public static final String MIMETYPE_NAME = "com.codegemz.elfi.coreapp.api";

    public static final int CONTENT_URI_PATTERN_MANY = 1;
    public static final int CONTENT_URI_PATTERN_ONE = 2;

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build();

    public static class Columns{
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String URL = "url";
    }

}
