package com.codegemz.elfi.coreapp.api.behavior_processor;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.codegemz.elfi.coreapp.BrainApp;
import com.codegemz.elfi.coreapp.helper.text_to_speech.TextToSpeechHelper;

/**
 * Created by adrobnych on 5/28/15.
 */
public class SpeakService extends IntentService {

    public SpeakService() {
        super("SpeakService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("SpeakService:", "onIntent");

        String text = intent.getStringExtra("text");

        TextToSpeechHelper tthelper =  ((BrainApp)getApplication()).getBrainActivity().getTthelper();
        tthelper.speakText(text);
        //tthelper.release();
    }
}
