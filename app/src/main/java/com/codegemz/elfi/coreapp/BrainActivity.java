package com.codegemz.elfi.coreapp;


import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper.Connector;
import com.codegemz.elfi.coreapp.helper.anti_echo.AntiEchoStateHelper;
import com.codegemz.elfi.coreapp.helper.api_ai.ApiAiHelper;
import com.codegemz.elfi.coreapp.helper.backbone_helper.BTForBrain;
import com.codegemz.elfi.coreapp.helper.emotion.EmotionStateHelper;
import com.codegemz.elfi.coreapp.helper.face_detection.CamPreviewView;
import com.codegemz.elfi.coreapp.helper.indoor_location.WifiObserver;
import com.codegemz.elfi.coreapp.helper.indoor_location.WifiScanReceiver;
import com.codegemz.elfi.coreapp.helper.text_to_speech.TextToSpeechHelper;
import com.codegemz.elfi.coreapp.helper.voice_recognition.TalkStateHelper;
import com.codegemz.elfi.coreapp.helper.voice_recognition.VoiceRecoHelper;
import com.codegemz.elfi.coreapp.helper.voice_recognition.VoiceRecognitionListener;
import com.codegemz.elfi.coreapp.helper.workflow.WorkflowStateHelper;
import com.codegemz.elfi.coreapp.helper.xmpp.XMPPClientBinder;
import com.codegemz.elfi.model.EmojiManager;
import com.codegemz.elfi.model.TalkStateType;
import com.codegemz.elfi.model.WorkflowType;
import com.quickblox.chat.QBChatService;

import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import ai.api.AIConfiguration;
import ai.api.AIDataService;


public class BrainActivity extends BaseActivity {
    private static final String TAG = "ELFi.CORE.";
    private CamPreviewView mCamPreviewView;
    private ContentResolver cr;
    public  CountDownLatch testingLatch;
    private EmotionStateHelper emotionStateHelper;

    public TextToSpeechHelper getTthelper() {
        return tthelper;
    }

    private TalkStateHelper talkStateHelper;
    private ImageView iv;
    private TextToSpeechHelper tthelper;
    private TextView tv;

    public WifiManager getWifi() {
        return wifi;
    }

    private WifiManager wifi;

    public BroadcastReceiver getWifiReciever() {
        return wifiReciever;
    }

    private  BroadcastReceiver wifiReciever;

    //public XMPPClientBinder getXmppBinder() {
    //    return xmppBinder;
    //}

    //@Inject
    //XMPPClientBinder xmppBinder;

    public CamPreviewView getCamPreviewView(){
        return mCamPreviewView;
    }
    public TalkStateHelper getTalkStateHelper(){ return talkStateHelper; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tthelper = new TextToSpeechHelper(this);

        mCamPreviewView = new CamPreviewView(this);

        setContentView(R.layout.activity_brain);

        iv = (ImageView) findViewById(R.id.imageView);
        tv = (TextView) findViewById(R.id.speech_result);

        cr = getContentResolver();
        emotionStateHelper = new EmotionStateHelper(this);
        emotionStateHelper.initializeEmotionSate();
        talkStateHelper = new TalkStateHelper(this);
        talkStateHelper.setTalkSate(TalkStateType.Inactive);
        tv.setText(R.string.listening_disabled);

        updateFace();

        FrameLayout previewHolder = (FrameLayout) findViewById(R.id.preview_holder);
        previewHolder.addView(mCamPreviewView);
        previewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPreviewClick();
            }
        });

        ((BrainApp)getApplication()).setBrainActivity(this);

        final AIConfiguration config = new AIConfiguration(ApiAiHelper.ACCESS_TOKEN, ApiAiHelper.SUBSCRIPTION_KEY,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        (new AntiEchoStateHelper(this)).createDefaultEchoSet();

        wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        //xmppBinder.initWithContext(this).processLogin();

    }

    private void onPreviewClick() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("DEMO MODE");

        // set dialog message
        alertDialogBuilder
                .setMessage("Set some phrase for execution!")
                .setCancelable(false);


        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.demo_dialog_layout, null);


        final AutoCompleteTextView textView = (AutoCompleteTextView) dialoglayout.findViewById(R.id.actv);
        String[] commands = getResources().getStringArray(R.array.demo_commands);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, commands);
        textView.setAdapter(adapter);
        final Spinner spin = (Spinner) dialoglayout.findViewById(R.id.spinner);

        alertDialogBuilder.setView(dialoglayout);

        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO: schedule new voice command
                final String command = textView.getText().toString();
                int delay = new Integer(spin.getSelectedItem().toString());
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        //getVrl().analyse(command);
                    }
                };
                handler.postDelayed(runnable, delay * 1000);
            }
        })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private boolean appWasExpired() {
        long expirationDate = (new GregorianCalendar(2018,6,10)).getTimeInMillis();
        if(System.currentTimeMillis() > expirationDate)
            return true;
        else
            return false;
    }

    private boolean appSNDontMatch() {
        Log.e("TAG","android.os.Build.SERIAL: " + Build.SERIAL);
        if(!Build.SERIAL.toUpperCase().equals("0A17CA1E"))//"R52H20ND0CM"))
            return true;
        else
            return false;
    }


    private boolean resumingPending;

    @Override
    protected void onResume() {
        super.onResume();
        if(appWasExpired()) {
            Toast.makeText(this, "This app was expired! Refer to www.elfirobotics.com", Toast.LENGTH_LONG).show();
            finish();
        }
//        if(appSNDontMatch()){
//            Toast.makeText(this, "This app binded to another SN! Refer to www.elfirobotics.com", Toast.LENGTH_LONG).show();
//            finish();
//        }
        //before we grab the camera, check to see if the lock screen is up
        KeyguardManager myKM = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode()) {
            resumingPending = true;
            Log.d(TAG,"screenLocked");
        } else {
            mCamPreviewView.obtainCamera();
            resumingPending = false;
            Log.d(TAG,"!screenLocked");
        }

//        if (!QBChatService.isInitialized()) {
//            QBChatService.init(this);
//        }

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        mCamPreviewView.releaseCamera();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //((BrainApp)getApplication()).setBrainActivity(null);
        try{
            tthelper.release();
        }
        catch (Exception e) {
            Log.e(TAG, "Exception:" + e.toString());
        }
    }

    public static void beep(){
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
    }

    public void onFaceClick(View v){
        changeListeningMode();
        //stop motors

        WorkflowStateHelper wsh = new WorkflowStateHelper(this);
        wsh.createOrUpdate(WorkflowType.DANCE, "", "stop");
        stopService(new Intent(this, MusicService.class));

    }

    private void changeListeningMode() {
        if(talkStateHelper.getTalkState() == TalkStateType.Inactive)
            getCamPreviewView().startTalk();
        else {
            TalkStateHelper tsh = new TalkStateHelper(this);
            tsh.setTalkSate(TalkStateType.Inactive);
        }
    }

    public void updateFace() {

        if(iv.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable frameAnimation = (AnimationDrawable) iv.getBackground();
            frameAnimation.stop();
            frameAnimation = null;
            //System.gc();
        }

        iv.setBackgroundResource((new EmojiManager()).getEmojiDrawableId(
                emotionStateHelper.getCurrentEmotionType(),
                emotionStateHelper.getCurrentEmotionLevel()));

        //Log.e("drw instance", "" + iv.getBackground().getClass());

        if(iv.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable frameAnimation = (AnimationDrawable) iv.getBackground();
            frameAnimation.start();
        }
    }

    public void startWifiScannerAndNotify(WifiObserver wo) {

        wifiReciever = new WifiScanReceiver(wo);
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifi.startScan();
    }


}
