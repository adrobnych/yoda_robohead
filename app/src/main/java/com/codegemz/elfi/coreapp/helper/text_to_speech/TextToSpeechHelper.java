package com.codegemz.elfi.coreapp.helper.text_to_speech;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.codegemz.elfi.coreapp.BrainActivity;
import com.codegemz.elfi.coreapp.R;
import com.codegemz.elfi.coreapp.helper.emotion.EmotionStateHelper;
import com.codegemz.elfi.apicontracts.EmojiType;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;


public class TextToSpeechHelper implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    private final TextToSpeech mTextToSpeech;
    // Used to queue up messages before the TTS engine is initialized...
    private final ConcurrentLinkedQueue<String> mBufferedMessages;
    private Context mContext;
    private boolean mIsReady;

    public TextToSpeechHelper(Context context) {
        mContext = context;
        mBufferedMessages = new ConcurrentLinkedQueue<String>();
        mTextToSpeech = new TextToSpeech(mContext, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTextToSpeech.setLanguage(Locale.ENGLISH);
            synchronized (this) {
                mIsReady = true;
                for (String bufferedMessage : mBufferedMessages) {
                    speakText(bufferedMessage);
                }
                mBufferedMessages.clear();
            }
            mTextToSpeech.setOnUtteranceCompletedListener(this);
        }
    }

    public void release() {
        synchronized (this) {
            mTextToSpeech.shutdown();
            mIsReady = false;
        }
    }

    public void notifyNewMessages(int messageCount) {
        String message = mContext.getResources().
                getQuantityString(R.plurals.messages,
                        messageCount, messageCount);
        synchronized (this) {
            if (mIsReady) {
                speakText(message);
            } else {
                mBufferedMessages.add(message);
            }
        }
    }

    public void speakText(String message) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                "STREAM_NOTIFICATION");
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
        mTextToSpeech.speak(message, TextToSpeech.QUEUE_ADD, params);
        mTextToSpeech.playSilence(100, TextToSpeech.QUEUE_ADD, params);
    }

    //current echo strategy
    public boolean isSpeaking(){
        return mTextToSpeech.isSpeaking();
    }

    //TODO: ivestigate if this is better echo strategy
    @Override
    public void onUtteranceCompleted(String utteranceId) {
        Log.e("talk finished", "stop________emo");
        EmotionStateHelper emotionStateHelper = new EmotionStateHelper(mContext);
        emotionStateHelper.setCurrentEmotionType(EmojiType.Happy);
        emotionStateHelper.setCurrentEmotionLevel(0);
        ((BrainActivity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((BrainActivity)mContext).updateFace();
            }
        });

    }
}