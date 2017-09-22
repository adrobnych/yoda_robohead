package com.codegemz.elfi.coreapp.api.external__algorithms;

import com.codegemz.elfi.apicontracts.AlgorithmBundleContract;
import com.codegemz.elfi.model.Algorithm.AlgorithmBundle;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;
import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd;

/**
 * Created by adrobnych on 5/30/15.
 */
public class AlgorithmBundleProvider extends OrmLiteSimpleContentProvider<ContentProviderDBHelper> {
    @Override
    protected Class<ContentProviderDBHelper> getHelperClass() {
        return ContentProviderDBHelper.class;
    }

    @Override
    public boolean onCreate() {
        setMatcherController(new MatcherController()//
                .add(AlgorithmBundle.class, MimeTypeVnd.SubType.DIRECTORY, "", AlgorithmBundleContract.CONTENT_URI_PATTERN_MANY)//
                .add(AlgorithmBundle.class, MimeTypeVnd.SubType.ITEM, "#", AlgorithmBundleContract.CONTENT_URI_PATTERN_ONE));
        return true;
    }
}
