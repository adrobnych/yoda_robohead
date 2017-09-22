package com.codegemz.elfi.model.Algorithm;

import android.provider.BaseColumns;

import com.codegemz.elfi.apicontracts.AlgorithmStepContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation;

/**
 * Created by adrobnych on 5/28/15.
 */
@DatabaseTable(tableName = AlgorithmStepContract.TABLE_NAME)
@AdditionalAnnotation.DefaultContentUri(authority = AlgorithmStepContract.AUTHORITY, path = AlgorithmStepContract.CONTENT_URI_PATH)
@AdditionalAnnotation.DefaultContentMimeTypeVnd(name = AlgorithmStepContract.MIMETYPE_NAME, type = AlgorithmStepContract.MIMETYPE_TYPE)
public class AlgorithmStep {

    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @AdditionalAnnotation.DefaultSortOrder
    private int id;

    @DatabaseField
    private int priority;

    @DatabaseField
    private String intent;

    @DatabaseField
    private String value1;

    @DatabaseField
    private String value2;

    public String getAlgorithm_name() {
        return algorithm_name;
    }

    public void setAlgorithm_name(String algorithm_name) {
        this.algorithm_name = algorithm_name;
    }

    @DatabaseField(index = true, canBeNull = false)
    private String algorithm_name;


    public AlgorithmStep() {
        // ORMLite needs a no-arg constructor
    }

    public AlgorithmStep(String intent) {
        this.id = 0;
        this.intent = intent;
    }

    public int getId() {
        return id;
    }

    public String getIntent() {
        return intent;
    }



}

