package com.codegemz.elfi.coreapp.helper.voice_recognition;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

import com.codegemz.elfi.coreapp.BrainActivity;
import com.codegemz.elfi.coreapp.R;
import com.codegemz.elfi.coreapp.api.behavior_processor.ApiAiCommandConfigurator;
import com.codegemz.elfi.coreapp.api.behavior_processor.SimpleReflexConfigurator;
import com.codegemz.elfi.coreapp.helper.anti_echo.AntiEchoStateHelper;
import com.codegemz.elfi.coreapp.helper.emotion.EmotionStateHelper;
import com.codegemz.elfi.coreapp.helper.text_to_speech.TextToSpeechHelper;
import com.codegemz.elfi.apicontracts.EmojiType;
import com.codegemz.elfi.model.TalkStateType;

import java.util.ArrayList;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;

/**
 * Created by adrobnych on 5/27/15.
 */
public class VoiceRecognitionListener implements RecognitionListener {
    TextView tv;
    TextToSpeechHelper tthelper;
    TalkStateHelper tsh;
    Context ctx;
    private AntiEchoStateHelper aeStateHelper;
    private SimpleReflexConfigurator simpleReflexConfigurator;
    private ApiAiCommandConfigurator apiAiCommandConfigurator;
    private AIDataService aiDataService;

    private boolean ttsIsBusy;

    public VoiceRecognitionListener(TextView tv, TextToSpeechHelper tthelper, Context ctx) {
        this.tv = tv;
        this.tthelper = tthelper;
        this.tsh = ((BrainActivity)ctx).getTalkStateHelper();
        this.ctx = ctx;
        simpleReflexConfigurator = new SimpleReflexConfigurator(ctx);
        apiAiCommandConfigurator = new ApiAiCommandConfigurator(ctx);
        aeStateHelper = new AntiEchoStateHelper(ctx);
        simpleReflexConfigurator.loadReflexes();
        apiAiCommandConfigurator.loadCommands();
        //aiDataService = ((BrainActivity)ctx).getAIDataService();
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
    }

    @Override
    public void onBeginningOfSpeech() {

        tv.setText("");
        if(tthelper.isSpeaking())
            ttsIsBusy = true;
        else
            ttsIsBusy = false;
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        // Not used
    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        //repeatOrStopReco();
    }

    @Override
    public void onError(int i) {
        //if(!simpleReflexConfigurator.isItAQuiz())
            repeatOrStopReco();

    }

    private void repeatOrStopReco(){
        //next reco session if it's less then 5 min since last Face seen
        if(tsh.getTalkState() == TalkStateType.Active
                && (System.currentTimeMillis() - tsh.getTalkStateLastTimeUpdate())/1000 < 300){
            Intent startRecoIntent = new Intent(VoiceRecoHelper.START_RECO_NOTIFICATION);
            ctx.sendBroadcast(startRecoIntent);
        }
        else{
            tsh.setTalkSate(TalkStateType.Inactive);
            tv.setText(R.string.listening_disabled);
        }
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> partialResults =
                bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (partialResults != null && partialResults.size() > 0 && tsh.getTalkState() == TalkStateType.Active) {
            String bestResult = partialResults.get(0);
            tv.setText(bestResult + ".");
            tsh.setTalkSate(TalkStateType.Active); // touch last time
            if((!aeStateHelper.isEcho(bestResult)) && (!ttsIsBusy))
                analyse(bestResult);
            else {
                if (!simpleReflexConfigurator.isItAQuiz())
                    aeStateHelper.unsetEchoState();
            }
            repeatOrStopReco();
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        ArrayList<String> partialResults =
                bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (partialResults != null && partialResults.size() > 0) {
            String bestResult = partialResults.get(0);
            tv.setText(bestResult);
        }
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        // Not used...
    }


    public void analyse(String lastSentence) {
        lastSentence = lastSentence.toLowerCase();
        Log.e("VRECO is it command...", lastSentence);
        if(simpleReflexConfigurator.isItAQuiz()) {
            //tsh.setTalkSate(TalkStateType.Inactive);
            simpleReflexConfigurator.performQuiz(lastSentence);
        }
        else {
            if(!simpleReflexConfigurator.findMatchAndExecute(lastSentence))
                tryApiAi(lastSentence);
        }
    }


    public void tryApiAi(String lastSentence){

        new AITask().execute(lastSentence);

    }

    private class AITask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String lastSentence = params[0];

            final AIRequest aiRequest = new AIRequest();
            aiRequest.setQuery(lastSentence);

            try {
                final AIResponse aiResponse = aiDataService.request(aiRequest);
                final Result result = aiResponse.getResult();

                if( result.getFulfillment().getSpeech() != null &&
                    result.getFulfillment().getSpeech() != "" ) {
                    Intent intent = new Intent("com.codegemz.elfi.coreapp.api.SPEAK");
                    intent.putExtra("text", result.getFulfillment().getSpeech());
                    ctx.sendBroadcast(intent);

                    EmotionStateHelper emotionStateHelper = new EmotionStateHelper(ctx);
                    emotionStateHelper.setCurrentEmotionType(EmojiType.Talk);
                    emotionStateHelper.setCurrentEmotionLevel(0);
                    ((BrainActivity) ctx).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((BrainActivity) ctx).updateFace();
                        }
                    });
                }
                else {
                    if (result.getAction() != null)
                        apiAiCommandConfigurator.findMatchAndExecute(result);
                    final Metadata metadata = result.getMetadata();
                    if (metadata != null && metadata.getIntentName()==null)
                        tthelper.speakText("I need more information.");
                }

            } catch (final AIServiceException e) {
                e.printStackTrace();
            }
            return null;
        }


    }


}
