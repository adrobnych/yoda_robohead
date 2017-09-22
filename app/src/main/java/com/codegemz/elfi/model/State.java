package com.codegemz.elfi.model;

/**
 * Created by adrobnych on 5/24/15.
 */

import android.provider.BaseColumns;

import com.codegemz.elfi.apicontracts.StateContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;


@DatabaseTable(tableName = StateContract.TABLE_NAME)
@DefaultContentUri(authority = StateContract.AUTHORITY, path = StateContract.CONTENT_URI_PATH)
@DefaultContentMimeTypeVnd(name = StateContract.MIMETYPE_NAME, type = StateContract.MIMETYPE_TYPE)
public class State {

    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @DefaultSortOrder
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String value1;

    @DatabaseField
    private String value2;

    public State() {
        // ORMLite needs a no-arg constructor
    }

    public State(String name, String value1, String value2) {
        this.id = 0;
        this.name = name;
        this.value1 = value1;
        this.value2 = value2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
