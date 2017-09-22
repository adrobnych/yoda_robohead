package com.codegemz.elfi.coreapp.api.behavior_processor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.codegemz.elfi.coreapp.BrainActivity;
import com.codegemz.elfi.coreapp.BrainApp;
import com.codegemz.elfi.coreapp.MusicService;
import com.codegemz.elfi.coreapp.SettingsActivity;
import com.codegemz.elfi.apicontracts.StateContract;
import com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper.Connector;
import com.codegemz.elfi.coreapp.helper.algorithm.AlgorithmExecutor;
import com.codegemz.elfi.coreapp.helper.anti_echo.AntiEchoStateHelper;
import com.codegemz.elfi.coreapp.helper.state.StateHelperModule;
import com.codegemz.elfi.coreapp.helper.voice_recognition.TalkStateHelper;
//import com.codegemz.elfi.coreapp.helper.voice_recognition.VoiceRecoHelper;
import com.codegemz.elfi.coreapp.helper.workflow.WorkflowStateHelper;
import com.codegemz.elfi.model.EmojiManager;
import com.codegemz.elfi.apicontracts.EmojiType;
//import com.codegemz.elfi.model.TalkStateType;
import com.codegemz.elfi.model.TalkStateType;
import com.codegemz.elfi.model.WorkflowType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by adrobnych on 6/7/15.
 */
public class SimpleReflexConfigurator {

    public HashMap<String, SimpleReflexCommand> simpleReflexMap;
    public HashMap<String, SimpleReflexCommand> simpleTalkMap;
    private Context context;
    public SimpleReflexConfigurator(Context context){
        this.context = context;
        simpleReflexMap = new HashMap<>();
        simpleTalkMap = new HashMap<>();
    }

    private Intent intent;

    public boolean isItAQuiz(){
        if(StateHelperModule.numOfRecords(context.getContentResolver(),
                new String[]{WorkflowType.CONVERSATION_QUIZ_SCORE.toString()}) > 0)
            return true;
        else
            return false;
    }

    public void performQuiz(String lastSentence)
    {
        Cursor cursor = context.getContentResolver().query(
                StateContract.CONTENT_URI, null,
                "name = ?",
                new String[]{WorkflowType.CONVERSATION_QUIZ_OPTIONS.toString()}, null);
        //cursor.moveToFirst();

        boolean foundMatch = false;
        boolean result = false;

        while(cursor.moveToNext()){
            String pattern = cursor.getString(
                    cursor.getColumnIndex(StateContract.Columns.VALUE1));
            boolean isTrue = new Boolean(cursor.getString(
                    cursor.getColumnIndex(StateContract.Columns.VALUE2))).booleanValue();
            Log.e("quiz :"+lastSentence, " pattern :"+pattern);
            if( lastSentence.matches(pattern) ) {

                foundMatch = true;
                result = isTrue;
            }
        }

        cursor.close();

        if(foundMatch){
            sayConclusion(result);

        }
        else
            CannedSteps.say("I didn't catch you well. Please select one of options!",
                    "^.*(i didn't|options).*$", context);
    }

    private void sayConclusion(boolean result) {
        WorkflowStateHelper wsh = new WorkflowStateHelper(context);
        if(result == true) {
            CannedSteps.say("You won this round!", null, context);
            wsh.increaseScore();
        }
        else
            CannedSteps.say("Ha Ha! You loose this round!", null, context);

        //going to the next step
        Cursor cursor = context.getContentResolver().query(
                StateContract.CONTENT_URI, null,
                "name = ?",
                new String[]{WorkflowType.CONVERSATION_QUIZ_NEXT_STEP.toString()}, null);
        cursor.moveToFirst();
        if(cursor.getCount() == 0) {
            CannedSteps.say("You've done with this quiz!", null,  context);
            sayResults();
            wsh.remove(WorkflowType.CONVERSATION_QUIZ_OPTIONS);
            wsh.remove(WorkflowType.CONVERSATION_QUIZ_NEXT_STEP);
            wsh.remove(WorkflowType.CONVERSATION_QUIZ_SCORE);
        }
        else{
            simpleTalkMap.get(wsh.getNameOfTheNextStep()).perform();

        }

    }

    private void sayResults() {
        WorkflowStateHelper wsh = new WorkflowStateHelper(context);
        CannedSteps.setMaxEmotion(EmojiType.Happy, context);
        CannedSteps.say("your result is " +
                wsh.getRating(), null, context);
    }



    public void loadReflexes(){

//        simpleReflexMap.put("hello.*", new SimpleReflexCommand() {
//            @Override
//            public void perform() {
//                CannedSteps.setMaxEmotion(EmojiType.Happy, context);
//                CannedSteps.say("Hello! It's nice to meet you!", "hello.*", context);
//            }
//        });

        simpleReflexMap.put("poltergeist", new SimpleReflexCommand() {
            @Override
            public void perform() {

                ((BrainActivity)context).getXmppBinder().listenAndAnswer();
                Log.e("SimpleReflexConfig: ", "Poltergeist started");
            }
        });

        simpleReflexMap.put("what time is it", new SimpleReflexCommand() {

            private String INTENT_TO_WAIT = "com.codegemz.elfi.coreapp.api.behavior_processor.AFTER_TIME_SAID";

            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Happy, context);
                CannedSteps.say("It is " + time(), null, context);
//                sayTimeAndFireIntent(INTENT_TO_WAIT);
//                CannedSteps.waitFor(INTENT_TO_WAIT, context, new SimpleReflexCommand() {
//                            @Override
//                            public void perform() {
//                                CannedSteps.setMaxEmotion(EmojiType.Happy, context);
//                            }
//                        });
            }

//            private void sayTimeAndFireIntent(String intent_to_wait) {
//                CannedSteps.sayAndWait(intent_to_wait);
//            }

            private String time(){
                String result = "";
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh.mm aa");
                String formattedTime= dateFormat1.format(new Date()).toString();
                result += formattedTime;
                return result;
            }

        });


        simpleReflexMap.put("what day is today", new SimpleReflexCommand() {

            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Playful, context);
                CannedSteps.say("Today is " + date(), null, context);
            }

            private String date(){
                String result = "";
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("M-dd-yyyy");
                String formattedDate= dateFormat2.format(new Date()).toString();
                result += "..." + formattedDate;
                return result;
            }

        });

        simpleReflexMap.put("hi.*", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Happy, context);
                CannedSteps.say("Hello! It's nice to meet you!", null, context);
            }
        });

        simpleReflexMap.put("settings", new SimpleReflexCommand() {
            @Override
            public void perform() {
                Intent i = new Intent(context, SettingsActivity.class);
                context.startActivity(i);
            }
        });

        simpleReflexMap.put("play music.*", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Happy, context);
                CannedSteps.playSong(context);
            }
        });

        simpleReflexMap.put(".*(dancing time|party time).*", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Happy, context);
                CannedSteps.playSong(context);
                new AlgorithmExecutor(context).findAndPerform("party");
            }
        });

        simpleReflexMap.put("stop music.*", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Happy, context);
                CannedSteps.stopSong(context);
            }
        });

        simpleReflexMap.put(".*bad robot", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Sad, context);
                CannedSteps.say("Oh no! ", null, context);
            }
        });

        simpleReflexMap.put(".*good robot", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Loving, context);
                CannedSteps.say("Wow! I'm extremely glad to hear this", null, context);
            }
        });

        simpleReflexMap.put(".*good droid", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Loving, context);
                CannedSteps.say("Wow! I'm extremely glad to hear this", null, context);
            }
        });

//        simpleReflexMap.put("what is your name", new SimpleReflexCommand() {
//            @Override
//            public void perform() {
//                CannedSteps.setMaxEmotion(EmojiType.Happy, context);
//                CannedSteps.say("My name is ELFi", null, context);
//            }
//        });

        simpleReflexMap.put("what kind of robot is you", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Happy, context);
                CannedSteps.say("I am extensible home robot with open api", null, context);
            }
        });

        simpleReflexMap.put("what do you want", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Playful, context);
                CannedSteps.say("Please buy me some new app on robomarket", null, context);
            }
        });

        simpleReflexMap.put("show me a video about you", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Playful, context);
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=7U2avyKq0eM")));
            }
        });

        simpleReflexMap.put("show me other robots.*", new SimpleReflexCommand() {
            @Override
            public void perform() {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.elfirobotics.videostory", "com.elfirobotics.videostory.MainActivity"));
                context.startActivity(intent);
            }
        });

        simpleReflexMap.put("read me a book.*", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Playful, context);
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=c1CmCov9PKw")));
            }
        });


        simpleReflexMap.put("give me a drink", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Surprised, context);
                Connector connector = ((BrainApp) context.getApplicationContext()).getConnector();
                if(connector.connect())
                    connector.writeSingleMessage("motor_Arduino_FORWARD");
                else{
                    Log.d(connector.getTag(), "BT connection failed");
                }
            }
        });

        simpleReflexMap.put("droid go forward", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Ninja, context);
                Connector connector = ((BrainApp) context.getApplicationContext()).getConnector();
                if(connector.connect())
                    connector.writeSingleMessage("motor_Arduino_FORWARD");
                else{
                    Log.d(connector.getTag(), "BT connection failed");
                }
            }
        });

        simpleReflexMap.put("droid go left", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Ninja, context);
                Connector connector = ((BrainApp) context.getApplicationContext()).getConnector();
                if(connector.connect())
                    connector.writeSingleMessage("motor_Arduino_LEFT");
                else{
                    Log.d(connector.getTag(), "BT connection failed");
                }
            }
        });

        simpleReflexMap.put("droid go right", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Ninja, context);
                Connector connector = ((BrainApp) context.getApplicationContext()).getConnector();
                if(connector.connect())
                    connector.writeSingleMessage("motor_Arduino_RIGHT");
                else{
                    Log.d(connector.getTag(), "BT connection failed");
                }
            }
        });

        simpleReflexMap.put("(follow this target|follow this|follow the ball)", new SimpleReflexCommand() {
            @Override
            public void perform() {
                ((BrainActivity)context).setTargetFollowing(true);
            }
        });

        simpleReflexMap.put("(guard the house|enable security)", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Ninja, context);
                new AlgorithmExecutor(context).findPerformAndWaitForResult("security");
            }
        });

        simpleReflexMap.put("go sleep.*", new SimpleReflexCommand() {
            @Override
            public void perform() {
                TalkStateHelper tsh = new TalkStateHelper(context);
                tsh.setTalkSate(TalkStateType.Inactive);
            }
        });

        simpleReflexMap.put("(stop|break)", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Happy, context);
                WorkflowStateHelper wsh = new WorkflowStateHelper(context);
                wsh.createOrUpdate(WorkflowType.DANCE, "", "stop");
                Connector connector = ((BrainApp) context.getApplicationContext()).getConnector();
                if(connector.connect())
                    connector.writeSingleMessage("motor_AB_STOP");
                else{
                    Log.d(connector.getTag(), "BT connection failed");
                }
            }
        });

        //ELFi quiz start
        simpleReflexMap.put("let's play", new SimpleReflexCommand() {
            @Override
            public void perform() {
                //TalkStateHelper tsh = ((BrainActivity)context).getTalkStateHelper();
                //tsh.setTalkSate(TalkStateType.Inactive);

                WorkflowStateHelper wsh = new WorkflowStateHelper(context);
                wsh.createOrUpdate( WorkflowType.CONVERSATION_QUIZ_SCORE,
                                    /*right answers:*/"0",
                                    /*total questions:*/"3");
                wsh.remove(WorkflowType.CONVERSATION_QUIZ_OPTIONS);
                wsh.add(WorkflowType.CONVERSATION_QUIZ_OPTIONS, ".*home.*", "true");
                wsh.add(WorkflowType.CONVERSATION_QUIZ_OPTIONS, ".*space.*", "false");
                wsh.add(WorkflowType.CONVERSATION_QUIZ_OPTIONS, ".*military.*", "false");
                wsh.remove(WorkflowType.CONVERSATION_QUIZ_NEXT_STEP);
                wsh.add(WorkflowType.CONVERSATION_QUIZ_NEXT_STEP, "about LEGO", "N/A");

                CannedSteps.setMaxEmotion(EmojiType.Playful, context);

                CannedSteps.say("Cool! Lets play Questions and Answers game about ELFi..." +
                                "What kind of robot is ELFi? Please select the right option: " +
                                "a Home robot (first option)," +
                                "a Space robot (second option)," +
                                "or a Military robot (third option)?",
                        "(?i)^.*(cool|questions and answers|first option|second option|third option).*$", context);




                //tsh.setTalkSate(TalkStateType.Active);
                //CannedSteps.turnOnVoiceListeningAfterSomeTime(15000, context);

            }
        });

        //ELFi quiz next step
        simpleTalkMap.put("about LEGO", new SimpleReflexCommand() {
            @Override
            public void perform() {
//                TalkStateHelper tsh = ((BrainActivity)context).getTalkStateHelper();
//                tsh.setTalkSate(TalkStateType.Inactive);

                CannedSteps.setMaxEmotion(EmojiType.Playful, context);
                CannedSteps.say("So... The next question..." +
                        "From which constructor set ELFI was built? Please select the right option: " +
                        "LEGO Duplo (first option)," +
                        "LEGO Mindstorms (second option)," +
                        "or LEGO Star Wars (third option)?",
                        "(?i)^.*(round|question|which constructor|was built|first option|second option|third option).*$", context);
                WorkflowStateHelper wsh = new WorkflowStateHelper(context);
                wsh.remove(WorkflowType.CONVERSATION_QUIZ_OPTIONS);
                wsh.add(WorkflowType.CONVERSATION_QUIZ_OPTIONS, ".*duplo.*", "false");
                wsh.add(WorkflowType.CONVERSATION_QUIZ_OPTIONS, ".*mindstorms.*", "true");
                wsh.add(WorkflowType.CONVERSATION_QUIZ_OPTIONS, ".*star wars.*", "false");
                wsh.remove(WorkflowType.CONVERSATION_QUIZ_NEXT_STEP);
                wsh.add(WorkflowType.CONVERSATION_QUIZ_NEXT_STEP, "about brain", "N/A");

//                tsh.setTalkSate(TalkStateType.Active);
//                CannedSteps.turnOnVoiceListeningAfterSomeTime(5000, context);
            }
        });

        //ELFi quiz last step
        simpleTalkMap.put("about brain", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Playful, context);
                CannedSteps.say("So... The last question...", null, context);
                CannedSteps.say("What forms the ELFi's brain? Please select the right option: " +
                        "Microsoft Windows (first option)," +
                        "iOS (second option)," +
                        "or Android Operating System (third option)?",
                        "(?i)^.*(the last question|what forms|brain|first option|second option|third option).*$", context);
                WorkflowStateHelper wsh = new WorkflowStateHelper(context);
                wsh.remove(WorkflowType.CONVERSATION_QUIZ_OPTIONS);
                wsh.add(WorkflowType.CONVERSATION_QUIZ_OPTIONS, ".*windows.*", "false");
                wsh.add(WorkflowType.CONVERSATION_QUIZ_OPTIONS, ".*ios.*", "false");
                wsh.add(WorkflowType.CONVERSATION_QUIZ_OPTIONS, ".*android.*", "true");
                wsh.remove(WorkflowType.CONVERSATION_QUIZ_NEXT_STEP);

            }
        });

    /*

    Excursion scenario

     */

    simpleReflexMap.put("(excursion point 1)", new SimpleReflexCommand() {
        @Override
        public void perform() {
            CannedSteps.setMaxEmotion(EmojiType.Talk, context);
            CannedSteps.say("Uzhhorod Castle is an extensive citadel on a hill in Uzhhorod, Ukraine. It was built in a mixture of architectural styles and materials between the 13th and 18th centuries and figured heavily in the history of Hungary. The very name of Uzhhorod refers to the castle, translating as \"the Uzh castle\".",
                    null, context);
        }
    });

    simpleReflexMap.put("(excursion point 2)", new SimpleReflexCommand() {
        @Override
        public void perform() {
            CannedSteps.setMaxEmotion(EmojiType.Talk, context);
            CannedSteps.say("The castle was repeatedly rebuilt during its history. The first reliable sources about building labour in Uzhhorod castle refer to 13-18 c. and they are connected with the name of Istvan Drugeth. It is that time, when the palace building was almost finished in 1598, as the engraved date on the front of the main entrance to the castle and Drugeth’s family coat of arms testify. The castle was considerably fortified during 17 c. Modern walls and bastions of the castle (except for one) were built in 1653-1658. A bit later, but until 1691, one more bastion was completed to the main’construction from the north side. In 1692, when the owner of the castle became Miklos Bercheni, it was in a very neglected condition. A new owner did a lot to fortify its walls and tidy the palace up. The last changes in the castle were done under the leadership of an architect Stubich in 1709-1710. Loggia with arcades in inner courtyard of the palace was built at that time.",
                    null, context);
        }
    });

    simpleReflexMap.put("(excursion point 3)", new SimpleReflexCommand() {
        @Override
        public void perform() {
            CannedSteps.setMaxEmotion(EmojiType.Talk, context);
            CannedSteps.say("Here you can see the iron statue of Hercules killing the Hydra. Being the oldest statue in Uzhgorod, it was made by Kinne, the local artist, whose fame transcended the boundaries of the Carpathian region. Not far from it you can see another example of fine metal work - the sculpture of the Resting Hermes.",
                    null, context);
        }
    });

    /*

    Transformer

     */

        simpleReflexMap.put("(transform app|transform up|rise)", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Happy, context);
                new AlgorithmExecutor(context).findAndPerform("transformer up");
            }
        });

        simpleReflexMap.put("(transform down|squeeze)", new SimpleReflexCommand() {
            @Override
            public void perform() {
                CannedSteps.setMaxEmotion(EmojiType.Happy, context);
                new AlgorithmExecutor(context).findAndPerform("transformer down");
            }
        });

        // should be lat lin in loadReflexes()
        loadReflexesFromDB();

    }

    class SimpleQuestionAndAnswerCommandImpl implements SimpleReflexCommand {
        public SimpleQuestionAndAnswerCommandImpl(String answer) {
            this.answer = answer;
        }

        private String answer;

        @Override
        public void perform() {
            CannedSteps.setMaxEmotion(EmojiType.Talk, context);
            CannedSteps.say(answer, null, context);
        }
    }
    private void loadReflexesFromDB() {
        Cursor cursor = context.getContentResolver().query(StateContract.CONTENT_URI, null,
                "name = ?",
                new String[]{"question_and_answer"}, null);

        if(cursor == null || cursor.getCount() < 1)
            return;

        while(cursor.moveToNext()){
            simpleReflexMap.put(cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE1)).toLowerCase(),
                    new SimpleQuestionAndAnswerCommandImpl(cursor.getString(cursor.getColumnIndex(StateContract.Columns.VALUE2))));
        }

        for(String key : simpleReflexMap.keySet()){
            Log.e("######", key);
        }
    }

    private static class CannedSteps{

        private static Intent intent;

        public static void setMaxEmotion(EmojiType et, Context context) {
            // TODO: use http://greenrobot.org/eventbus/
            intent = new Intent("com.codegemz.elfi.coreapp.api.SET_EMOTION");
            intent.putExtra("emotion_type", et.toString());
            intent.putExtra("emotion_level", (new EmojiManager().getMaxEmotionLevel(et)));
            context.sendBroadcast(intent);
        }

        public static BroadcastReceiver waitFor(String intentToWait, Context context, final SimpleReflexCommand srCommand) {
            BroadcastReceiver br = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    srCommand.perform();
                    context.unregisterReceiver(this);
                }
            };

            context.registerReceiver(br, new IntentFilter(intentToWait));
            return br;
        }

        public static void say(String s, String echo, Context context) {
            AntiEchoStateHelper aeStateHelper = new AntiEchoStateHelper(context);
            if(echo == null)
                echo = s;
            //if(!aeStateHelper.isEcho(echo)) {
                aeStateHelper.setEchoState(echo);
                intent = new Intent("com.codegemz.elfi.coreapp.api.SPEAK");
                intent.putExtra("text", s);
                context.sendBroadcast(intent);
        }

        public static void fireIntentAfterSomeTime(Intent intent, long time, Context context){
            //don'tuse 0 for inent id:  http://stackoverflow.com/questions/20620266/alarmmanager-stops-working-in-android-4-4-2-using-setexact
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 111, intent, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            long now = System.currentTimeMillis();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                manager.set(AlarmManager.RTC_WAKEUP, now + time, pendingIntent);
            else
                manager.setExact(AlarmManager.RTC_WAKEUP, now + time, pendingIntent);
        }

        public static void sayAndWait(String intent_to_wait) {
        }

        public static void stopSong(Context context) {
            context.stopService(new Intent(context, MusicService.class));
        }

        public static void playSong(Context context) {
            context.startService(new Intent(context, MusicService.class));
        }
    }

    public boolean findMatchAndExecute(String lastSentence) {
        boolean result = false;
        for(String pattern : simpleReflexMap.keySet()){
            if( lastSentence.matches(pattern) ) {
                simpleReflexMap.get(pattern).perform();
                result = true;
            }
        }
        return result;
    }
}
