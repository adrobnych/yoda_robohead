package com.codegemz.elfi.coreapp.api.helpers;

import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.codegemz.elfi.apicontracts.StateContract;
import com.codegemz.elfi.coreapp.api.StateProvider;
import com.codegemz.elfi.coreapp.helper.anti_echo.AntiEchoStateHelper;
import com.codegemz.elfi.model.db.ContentProviderDBHelper;

/**
 * Created by adrobnych on 5/24/15.
 */
public class AntiEchoHelperTest extends ProviderTestCase2<StateProvider> {
    public static final String TAG = "StateProvider";
    private AntiEchoStateHelper aeStateHelper;

    MockContentResolver mMockResolver;

    /**
     * Constructor.
     *
     * @param providerClass     The class name of the provider under test
     * @param providerAuthority The provider's authority string
     */
    public AntiEchoHelperTest(Class<StateProvider> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public AntiEchoHelperTest() {
        //this.CPTester("com.example.myapp.MyContentProvider",AsanaProvider.class);
        super(StateProvider.class,"com.codegemz.elfi.coreapp.api.StateProvider");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(TAG, "setUp: ");

        getHelper().resetAllTables();

        StateProvider provider = new StateProvider();
        provider.attachInfo(getContext(), null);

        mMockResolver = new MockContentResolver();
        mMockResolver.addProvider(StateContract.AUTHORITY, provider);

        aeStateHelper = new AntiEchoStateHelper(getContext());
        aeStateHelper.createDefaultEchoSet();
    }

    private ContentProviderDBHelper getHelper() {
        return new ContentProviderDBHelper(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown:");
    }

    public void testSetEcho(){

        aeStateHelper.setEchoState("bla bla");

        assertTrue(aeStateHelper.isEcho("bla bla"));
    }

    public void testUnsetEcho(){

        aeStateHelper.setEchoState("bla bla");

        aeStateHelper.unsetEchoState();

        assertFalse(aeStateHelper.isEcho("bla bla"));
    }

}
