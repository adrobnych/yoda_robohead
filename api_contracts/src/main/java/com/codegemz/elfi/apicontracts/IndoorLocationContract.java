package com.codegemz.elfi.apicontracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by adrobnych on 5/28/15.
 */
public final class IndoorLocationContract implements BaseColumns {

    private IndoorLocationContract() {
        // Utility constructor
    }

    public static final String TABLE_NAME = "IndoorLocation";

    public static final String AUTHORITY = "com.codegemz.elfi.coreapp.api.indoor_location_provider";

    public static final String CONTENT_URI_PATH = "indoor_locations";

    public static final String MIMETYPE_TYPE = "indoor_locations";
    public static final String MIMETYPE_NAME = "com.codegemz.elfi.coreapp.api";

    public static final int CONTENT_URI_PATTERN_MANY = 1;
    public static final int CONTENT_URI_PATTERN_ONE = 2;

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build();

    public static class Columns{
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String WIFI_SSID = "wifi_ssid";
        public static final String WIFI_CORRECTION_COEFFICIENT = "wifi_correction_coefficient";
    }

}
