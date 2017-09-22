package com.codegemz.elfi.coreapp.api;

import com.codegemz.elfi.apicontracts.PhraseIntentContract;
import com.codegemz.elfi.model.PhraseIntent;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;
import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd;

/**
 * Created by adrobnych on 5/28/15.
 */
public class PhraseIntentProvider extends OrmLiteSimpleContentProvider<ContentProviderDBHelper> {
    @Override
    protected Class<ContentProviderDBHelper> getHelperClass() {
        return ContentProviderDBHelper.class;
    }

    @Override
    public boolean onCreate() {
        setMatcherController(new MatcherController()//
                .add(PhraseIntent.class, MimeTypeVnd.SubType.DIRECTORY, "", PhraseIntentContract.CONTENT_URI_PATTERN_MANY)//
                .add(PhraseIntent.class, MimeTypeVnd.SubType.ITEM, "#", PhraseIntentContract.CONTENT_URI_PATTERN_ONE));
        return true;
    }
}
