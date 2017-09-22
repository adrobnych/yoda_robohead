package com.codegemz.elfi.coreapp.api;

import com.codegemz.elfi.apicontracts.IndoorLocationContract;
import com.codegemz.elfi.model.IndoorLocation;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;
import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd;

/**
 * Created by adrobnych on 8/11/15.
 */
public class IndoorLocationProvider extends OrmLiteSimpleContentProvider<ContentProviderDBHelper> {
    @Override
    protected Class<ContentProviderDBHelper> getHelperClass() {
        return ContentProviderDBHelper.class;
    }

    @Override
    public boolean onCreate() {
        setMatcherController(new MatcherController()//
                .add(IndoorLocation.class, MimeTypeVnd.SubType.DIRECTORY, "", IndoorLocationContract.CONTENT_URI_PATTERN_MANY)//
                .add(IndoorLocation.class, MimeTypeVnd.SubType.ITEM, "#", IndoorLocationContract.CONTENT_URI_PATTERN_ONE));
        return true;
    }
}
