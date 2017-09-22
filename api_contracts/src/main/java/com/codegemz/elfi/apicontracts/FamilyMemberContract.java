package com.codegemz.elfi.apicontracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by adrobnych on 8/11/15.
 */
public class FamilyMemberContract implements BaseColumns {

    private FamilyMemberContract() {
        // Utility constructor
    }

    public static final String TABLE_NAME = "FamilyMember";

    public static final String AUTHORITY = "com.codegemz.elfi.coreapp.api.family_member_provider";

    public static final String CONTENT_URI_PATH = "family_members";

    public static final String MIMETYPE_TYPE = "family_members";
    public static final String MIMETYPE_NAME = "com.codegemz.elfi.coreapp.api";

    public static final int CONTENT_URI_PATTERN_MANY = 1;
    public static final int CONTENT_URI_PATTERN_ONE = 2;

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build();

    public static class Columns{
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String STATUS = "status";
        public static final String AGE = "age";
        public static final String COMPANY = "company";
        public static final String POSITION = "position";
        public static final String BT_ID = "bt_id";
    }

}