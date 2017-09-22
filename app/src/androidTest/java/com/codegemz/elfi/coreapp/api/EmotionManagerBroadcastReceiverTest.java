package com.codegemz.elfi.coreapp.api;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import com.codegemz.elfi.coreapp.BrainApp;
import com.codegemz.elfi.coreapp.api.behavior_processor.EmotionManagerBroadcastReceiver;
import com.codegemz.elfi.model.EmojiManager;
import com.codegemz.elfi.apicontracts.EmojiType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by adrobnych on 5/23/15.
 */


class TestContext extends MockContext
{
    public boolean called = false;
    private ContentResolver cr;
    private BrainApp brainApp;

    public TestContext(BrainApp brainApp, ContentResolver cr){
        this.brainApp = brainApp;
        this.cr = cr;
    }

    @Override
    public Context getApplicationContext() {
        called = true;
        return brainApp;
    }

    @Override
    public ContentResolver getContentResolver(){
       return mock(ContentResolver.class);
    }

}

public class EmotionManagerBroadcastReceiverTest extends AndroidTestCase {
    private TestContext mContext;

    private ContentResolver crMock;
    private BrainApp brainApp;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        crMock = mock(ContentResolver.class);
        brainApp = mock(BrainApp.class);
        mContext = new TestContext(brainApp, crMock);
    }

    public void testReceive() {
        EmotionManagerBroadcastReceiver r = new EmotionManagerBroadcastReceiver();

        EmojiType et = EmojiType.Playful;
        Intent intent = new Intent("com.codegemz.elfi.coreapp.api.SET_EMOTION");
        intent.putExtra("emotion_type", et.toString());
        intent.putExtra("emotion_level", (new EmojiManager().getMaxEmotionLevel(et)));

        r.onReceive(mContext, intent);

        assertTrue(mContext.called);
        verify(brainApp).getBrainActivity();  // expected for call "updateFace"

    }
}