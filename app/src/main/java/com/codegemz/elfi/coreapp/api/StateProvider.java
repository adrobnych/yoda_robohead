package com.codegemz.elfi.coreapp.api;

import com.codegemz.elfi.apicontracts.StateContract;
import com.codegemz.elfi.model.State;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;

/**
 * Created by adrobnych on 5/24/15.
 */


import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;

public class StateProvider extends OrmLiteSimpleContentProvider<ContentProviderDBHelper> {
    @Override
    protected Class<ContentProviderDBHelper> getHelperClass() {
        return ContentProviderDBHelper.class;
    }

    @Override
    public boolean onCreate() {
        setMatcherController(new MatcherController()//
                .add(State.class, SubType.DIRECTORY, "", StateContract.CONTENT_URI_PATTERN_MANY)//
                .add(State.class, SubType.ITEM, "#", StateContract.CONTENT_URI_PATTERN_ONE));
        return true;
    }
}
