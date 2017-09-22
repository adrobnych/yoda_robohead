package com.codegemz.elfi.model.Algorithm;

import android.provider.BaseColumns;


import com.codegemz.elfi.apicontracts.AlgorithmContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation;

/**
 * Created by adrobnych on 5/28/15.
 */
@DatabaseTable(tableName = AlgorithmContract.TABLE_NAME)
@AdditionalAnnotation.DefaultContentUri(authority = AlgorithmContract.AUTHORITY, path = AlgorithmContract.CONTENT_URI_PATH)
@AdditionalAnnotation.DefaultContentMimeTypeVnd(name = AlgorithmContract.MIMETYPE_NAME, type = AlgorithmContract.MIMETYPE_TYPE)
public class Algorithm {

    @DatabaseField(columnName = BaseColumns._ID, generatedId = true, index = true)
    @AdditionalAnnotation.DefaultSortOrder
    private int id;

    @DatabaseField(index = true, unique = true)
    private String name;

    @DatabaseField
    private String type;

    @DatabaseField
    private String group_name;

    @DatabaseField
    private String value1;

    @DatabaseField
    private String value2;

    @DatabaseField(index = true, canBeNull = false)
    private String algorithm_bundle_name;


    public Algorithm() {
        // ORMLite needs a no-arg constructor
    }

    public Algorithm(String name, String type, String algorithm_bundle_name) {
        this.id = 0;
        this.name = name;
        this.type = type;
        this.algorithm_bundle_name = algorithm_bundle_name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAlgorithm_bundle_name() { return algorithm_bundle_name; }

}

