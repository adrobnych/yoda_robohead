package com.codegemz.elfi.coreapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.codegemz.elfi.apicontracts.EmojiType;
import com.codegemz.elfi.coreapp.helper.anti_echo.AntiEchoStateHelper;
import com.codegemz.elfi.coreapp.helper.emotion.EmotionStateHelper;

public class TTSReceiver extends BroadcastReceiver {
    private static final String TEXT_TO_SAY_ACTION = "com.elfirobotics.beehive.sense_intents.TTS.TEXT_TO_SAY";

    public TTSReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if(action == TEXT_TO_SAY_ACTION) {
            String text = intent.getStringExtra("com.elfirobotics.beehive.sense_intents.extra.TEXT");
            if (text != null) {
                ((BrainApp) context.getApplicationContext()).getBrainActivity().getTthelper().speakText(text);

                EmotionStateHelper emotionStateHelper = new EmotionStateHelper(context);
                emotionStateHelper.setCurrentEmotionType(EmojiType.Talk);
                emotionStateHelper.setCurrentEmotionLevel(0);
                //TODO: move it to tthelper
                ((BrainApp) context.getApplicationContext()).getBrainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ((BrainApp) context.getApplicationContext()).getBrainActivity().updateFace();
                    }
                });

            }
            Log.e("TTSReceiver", "TTS intent received for text: " + text);
        }
    }
}
