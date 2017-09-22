package com.codegemz.elfi.coreapp.api;

import com.codegemz.elfi.apicontracts.FamilyMemberContract;
import com.codegemz.elfi.model.FamilyMember;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;
import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd;

/**
 * Created by adrobnych on 8/11/15.
 */
public class FamilyMemberProvider extends OrmLiteSimpleContentProvider<ContentProviderDBHelper> {
    @Override
    protected Class<ContentProviderDBHelper> getHelperClass() {
        return ContentProviderDBHelper.class;
    }

    @Override
    public boolean onCreate() {
        setMatcherController(new MatcherController()//
                .add(FamilyMember.class, MimeTypeVnd.SubType.DIRECTORY, "", FamilyMemberContract.CONTENT_URI_PATTERN_MANY)//
                .add(FamilyMember.class, MimeTypeVnd.SubType.ITEM, "#", FamilyMemberContract.CONTENT_URI_PATTERN_ONE));
        return true;
    }
}
