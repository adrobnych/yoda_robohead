package com.codegemz.elfi.coreapp.api.external__algorithms;

import com.codegemz.elfi.apicontracts.AlgorithmContract;
import com.codegemz.elfi.model.Algorithm.Algorithm;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;
import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd;

/**
 * Created by adrobnych on 5/28/15.
 */
public class AlgorithmProvider extends OrmLiteSimpleContentProvider<ContentProviderDBHelper> {
    @Override
    protected Class<ContentProviderDBHelper> getHelperClass() {
        return ContentProviderDBHelper.class;
    }

    @Override
    public boolean onCreate() {
        setMatcherController(new MatcherController()//
                .add(Algorithm.class, MimeTypeVnd.SubType.DIRECTORY, "", AlgorithmContract.CONTENT_URI_PATTERN_MANY)//
                .add(Algorithm.class, MimeTypeVnd.SubType.ITEM, "#", AlgorithmContract.CONTENT_URI_PATTERN_ONE));
        return true;
    }
}
