package com.codegemz.elfi.coreapp;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.ImageView;

import com.codegemz.elfi.apicontracts.StateContract;
import com.codegemz.elfi.coreapp.helper.emotion.EmotionStateHelper;
import com.codegemz.elfi.coreapp.helper.voice_recognition.TalkStateHelper;
import com.codegemz.elfi.model.EmojiManager;
import com.codegemz.elfi.apicontracts.EmojiType;
import com.codegemz.elfi.model.TalkStateType;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by adrobnych on 5/20/15.
 */
public class BrainActivityTest extends ActivityInstrumentationTestCase2<BrainActivity> {

    public BrainActivityTest() {
        super(BrainActivity.class);
    }

    private BrainActivity brainActivity;
    private ImageView iv;
    private ContentResolver cr;
    private EmotionStateHelper emotionStateHelper;
    private TalkStateHelper talkStateHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        brainActivity = getActivity();
        iv = (ImageView) brainActivity.findViewById(R.id.imageView);
        cr = getContext().getContentResolver();

        emotionStateHelper = new EmotionStateHelper(brainActivity);

        talkStateHelper = new TalkStateHelper(brainActivity);

    }

    @Override
    public void tearDown(){
        cr.delete(StateContract.CONTENT_URI, null, null);
    }

    public void testPreconditions() {
        assertNotNull("brainActivity is null", brainActivity);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void testShouldHaveSmileImage() {
        assertNotNull("imageView is null", iv);

        assertEquals(EmojiType.Happy,
                emotionStateHelper.getCurrentEmotionType());
        assertEquals(0,
                emotionStateHelper.getCurrentEmotionLevel());

    }

    public void testShouldNotSwitchFaceAfterClick(){
        onView(withId(R.id.imageView))
                .check(matches(isDisplayed()));

        EmojiType oldET = emotionStateHelper.getCurrentEmotionType();
        int oldEL = emotionStateHelper.getCurrentEmotionLevel();

        onView(withId(R.id.imageView))
                .perform(click());

        EmojiType newET = emotionStateHelper.getCurrentEmotionType();
        int newEL = emotionStateHelper.getCurrentEmotionLevel();

        assertTrue(oldET == newET && oldEL == newEL);

    }

    public void testShouldSwitchVoiceRecoAfterClick(){
        TalkStateType origTS = talkStateHelper.getTalkState();

        onView(withId(R.id.imageView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.imageView))
                .perform(click());

        assertTrue(origTS != talkStateHelper.getTalkState());
    }

    private Drawable getDrawableByEmojiTypeAndLevel(EmojiType et, int el){
        int drawableId = (new EmojiManager()).getEmojiDrawableId(et, el);
        if(drawableId == -1)
            fail();
        Drawable drawable =
                brainActivity.getResources().getDrawable(drawableId);
        return drawable;
    }

    @MediumTest
    public void testShouldProcessSET_EMOTIONIntent(){

        //implicit intent
        EmojiType et = EmojiType.Surprised;
        int el = 0;
        Intent intent = new Intent("com.codegemz.elfi.coreapp.api.SET_EMOTION");
        intent.putExtra("emotion_type", et.toString());
        intent.putExtra("emotion_level", el);

        //check if someone is listening for this intent
        PackageManager packageManager = brainActivity.getPackageManager();
        List<ResolveInfo> bReceivers = packageManager.queryBroadcastReceivers(intent,
                0);
        assertTrue(bReceivers.size() == 1);

        brainActivity.testingLatch = new CountDownLatch(1);
        brainActivity.getBaseContext().sendBroadcast(intent);

        onView(withId(R.id.imageView))
                .check(matches(isDisplayed()));

        //TODO: try refactor to http://stackoverflow.com/questions/28148743/how-to-use-espresso-idling-resource
        // http://dev.jimdo.com/2014/05/09/wait-for-it-a-deep-dive-into-espresso-s-idling-resources/

        try {
            brainActivity.testingLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(EmojiType.Surprised,
                emotionStateHelper.getCurrentEmotionType());
        assertEquals(0,
                emotionStateHelper.getCurrentEmotionLevel());

        //assertEquals(((BitmapDrawable) getDrawableByEmojiTypeAndLevel(et, el).getCurrent()).getBitmap(),
        //        ((BitmapDrawable) iv.getBackground().getCurrent()).getBitmap());
    }

    public void testShouldBeAbleToRecognizeVoice(){

        onView(withId(R.id.speech_result)).check(matches(isDisplayed()));
        onView(withId(R.id.speech_result)).check(matches(withText("Listening disabled")));
        TalkStateHelper tsm = new TalkStateHelper(brainActivity);
        assertEquals(TalkStateType.Inactive, tsm.getTalkState());

        brainActivity.getCamPreviewView().startTalk();

        onView(withId(R.id.speech_result)).check(matches(withText("Listening enabled")));
        assertEquals(TalkStateType.Active, tsm.getTalkState());

        long currentMillis = System.currentTimeMillis();

        assertTrue(currentMillis > tsm.getTalkStateLastTimeUpdate());

        // should touch last update time
        brainActivity.getCamPreviewView().startTalk();

        assertTrue(currentMillis < tsm.getTalkStateLastTimeUpdate());
    }

}
