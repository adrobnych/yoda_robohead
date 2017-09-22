package com.codegemz.elfi.model;

import android.provider.BaseColumns;

import com.codegemz.elfi.apicontracts.FamilyMemberContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation;

@DatabaseTable(tableName = FamilyMemberContract.TABLE_NAME)
@AdditionalAnnotation.DefaultContentUri(authority = FamilyMemberContract.AUTHORITY, path = FamilyMemberContract.CONTENT_URI_PATH)
@AdditionalAnnotation.DefaultContentMimeTypeVnd(name = FamilyMemberContract.MIMETYPE_NAME, type = FamilyMemberContract.MIMETYPE_TYPE)
public class FamilyMember {

    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @AdditionalAnnotation.DefaultSortOrder
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String status;

    @DatabaseField
    private int age;

    @DatabaseField
    private String company;

    @DatabaseField
    private String position;

    @DatabaseField
    private String bt_id;

    public FamilyMember() {
        // ORMLite needs a no-arg constructor
    }


    public FamilyMember(String name, String status, String company, String position, String bt_id) {
        this.id = 0;
        this.name = name;
        this.status = status;
        this.company = company;
        this.position = position;
        this.bt_id = bt_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public String getBt_id() {
        return bt_id;
    }
}
