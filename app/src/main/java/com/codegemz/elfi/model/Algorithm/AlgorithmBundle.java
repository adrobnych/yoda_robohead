package com.codegemz.elfi.model.Algorithm;

import android.provider.BaseColumns;

import com.codegemz.elfi.apicontracts.AlgorithmBundleContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation;

/**
 * Created by adrobnych on 5/30/15.
 */
@DatabaseTable(tableName = AlgorithmBundleContract.TABLE_NAME)
@AdditionalAnnotation.DefaultContentUri(authority = AlgorithmBundleContract.AUTHORITY, path = AlgorithmBundleContract.CONTENT_URI_PATH)
@AdditionalAnnotation.DefaultContentMimeTypeVnd(name = AlgorithmBundleContract.MIMETYPE_NAME, type = AlgorithmBundleContract.MIMETYPE_TYPE)
public class AlgorithmBundle {

    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @AdditionalAnnotation.DefaultSortOrder
    private int id;

    @DatabaseField(index = true, unique = true)
    private String name;

    @DatabaseField
    private String url;


    public AlgorithmBundle() {
        // ORMLite needs a no-arg constructor
    }

    public AlgorithmBundle(String name, String url) {
        this.id = 0;
        this.name = name;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

}

