package com.codegemz.elfi.coreapp.api.behavior_processor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.codegemz.elfi.coreapp.BrainActivity;
import com.codegemz.elfi.coreapp.BrainApp;
import com.codegemz.elfi.coreapp.helper.emotion.EmotionStateHelper;
import com.codegemz.elfi.apicontracts.EmojiType;

/**
 * Created by adrobnych on 5/23/15.
 */
public class EmotionManagerBroadcastReceiver extends BroadcastReceiver {
    private EmotionStateHelper emotionStateHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO: move all logic to intentservice
        emotionStateHelper = new EmotionStateHelper(context);
        EmojiType neededET = EmojiType.fromString(intent.getStringExtra("emotion_type"));
        BrainActivity brainActivity = null;
        int neededEL = intent.getIntExtra("emotion_level", -1);

        brainActivity = ((BrainApp) (context.getApplicationContext())).getBrainActivity();
        if(brainActivity != null) {
            emotionStateHelper.setCurrentEmotionType(neededET);
            emotionStateHelper.setCurrentEmotionLevel(neededEL);
            brainActivity.updateFace();
            if(brainActivity.testingLatch != null)
                brainActivity.testingLatch.countDown();
        }

    }
}
