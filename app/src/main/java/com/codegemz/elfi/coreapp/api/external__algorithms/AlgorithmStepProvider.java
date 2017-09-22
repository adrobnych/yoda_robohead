package com.codegemz.elfi.coreapp.api.external__algorithms;

import com.codegemz.elfi.apicontracts.AlgorithmStepContract;
import com.codegemz.elfi.model.Algorithm.AlgorithmStep;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;
import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd;

/**
 * Created by adrobnych on 5/28/15.
 */
public class AlgorithmStepProvider extends OrmLiteSimpleContentProvider<ContentProviderDBHelper> {
    @Override
    protected Class<ContentProviderDBHelper> getHelperClass() {
        return ContentProviderDBHelper.class;
    }

    @Override
    public boolean onCreate() {
        setMatcherController(new MatcherController()
                .add(AlgorithmStep.class, MimeTypeVnd.SubType.DIRECTORY, "", AlgorithmStepContract.CONTENT_URI_PATTERN_MANY)
                .add(AlgorithmStep.class, MimeTypeVnd.SubType.ITEM, "#", AlgorithmStepContract.CONTENT_URI_PATTERN_ONE));
        return true;
    }
}