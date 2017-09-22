package com.codegemz.elfi.coreapp.api.behavior_processor;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.codegemz.elfi.apicontracts.FamilyMemberContract;
import com.codegemz.elfi.coreapp.BrainApp;
import com.codegemz.elfi.coreapp.helper.algorithm.AlgorithmExecutor;
import com.codegemz.elfi.coreapp.helper.building_instruction.BuildingInstructionHelper;
import com.codegemz.elfi.coreapp.helper.family_member_helper.NewFamilyMemberStateHelper;
import com.codegemz.elfi.coreapp.helper.indoor_location.IndoorLocationHelper;
import com.codegemz.elfi.model.EmojiManager;
import com.codegemz.elfi.apicontracts.EmojiType;

import java.util.HashMap;
import java.util.LinkedList;

import ai.api.model.Result;

/**
 * Created by adrobnych on 8/11/15.
 */
public class ApiAiCommandConfigurator {
    HashMap<String, SimpleReflexCommand> apiAiCommandMap;
    private Context context;
    private NewFamilyMemberStateHelper NFMStateHelper;

    public Result getAiResult() {
        return aiResult;
    }

    public void setAiResult(Result aiResult) {
        this.aiResult = aiResult;
    }

    private Result aiResult;

    public ApiAiCommandConfigurator(Context context) {
        this.context = context;
        apiAiCommandMap = new HashMap<>();
        NFMStateHelper = new NewFamilyMemberStateHelper(context);
    }

    public void loadCommands() {

        apiAiCommandMap.put("startOfConversationAboutNewFamilyMember", new SimpleReflexCommand() {
            @Override
            public void perform() {
                //create new empty familymemberCP record (if there is no state with "new_family_member_conversation" )
                if(NFMStateHelper.isItNewFamilyMemberState()) {
                    Log.e("apiCommands","execution: $$$ startOfConversationAboutNewFamilyMember");
                    //save api ai context "new_family_member_conversation" as value1 and "familymeber_id" as value2 to stateCP
                    ContentValues v = new ContentValues(1);
                    v.put(FamilyMemberContract.Columns.NAME,        "TBD");
                    Uri uri = context.getContentResolver().insert(FamilyMemberContract.CONTENT_URI, v);
                    NFMStateHelper.createDefaultNewFamilyMemberStateWithFMID(uri.getLastPathSegment());
                }
            }
        });

        apiAiCommandMap.put("familyMemberSetName", new SimpleReflexCommand() {
            @Override
            public void perform() {
                //get familymember_id from stateCP
                long fmId = NFMStateHelper.getFamilyMemberIdFromStateCP();
                //update familymember record with received param "fmname"
                ContentValues v = new ContentValues(1);
                v.put(FamilyMemberContract.Columns.NAME, getAiResult().getStringParameter("fmname"));
                context.getContentResolver().update(FamilyMemberContract.CONTENT_URI, v, "_id=?",
                        new String[]{""+fmId});
            }
        });

        apiAiCommandMap.put("familyMemberSetRole", new SimpleReflexCommand() {
            @Override
            public void perform() {
                //get familymember_id from stateCP
                long fmId = NFMStateHelper.getFamilyMemberIdFromStateCP();
                //update familymember record with received param "fmname"
                ContentValues v = new ContentValues(1);
                v.put(FamilyMemberContract.Columns.STATUS, getAiResult().getStringParameter("FMRole"));
                context.getContentResolver().update(FamilyMemberContract.CONTENT_URI, v, "_id=?",
                        new String[]{""+fmId});
            }
        });

        apiAiCommandMap.put("familyMemberSetAge", new SimpleReflexCommand() {
            @Override
            public void perform() {
                //get familymember_id from stateCP
                long fmId = NFMStateHelper.getFamilyMemberIdFromStateCP();
                //update familymember record with received param "fmname"
                ContentValues v = new ContentValues(1);
                v.put(FamilyMemberContract.Columns.AGE, getAiResult().getStringParameter("fmage"));
                context.getContentResolver().update(FamilyMemberContract.CONTENT_URI, v, "_id=?",
                        new String[]{""+fmId});
            }
        });

        apiAiCommandMap.put("familyMemberSetCompanyName", new SimpleReflexCommand() {
            @Override
            public void perform() {
                //get familymember_id from stateCP
                long fmId = NFMStateHelper.getFamilyMemberIdFromStateCP();
                //update familymember record with received param "fmname"
                ContentValues v = new ContentValues(1);
                v.put(FamilyMemberContract.Columns.COMPANY, getAiResult().getStringParameter("fmcompanyname"));
                context.getContentResolver().update(FamilyMemberContract.CONTENT_URI, v, "_id=?",
                        new String[]{""+fmId});
            }
        });

        apiAiCommandMap.put("familyMemberSetJob", new SimpleReflexCommand() {
            @Override
            public void perform() {
                //get familymember_id from stateCP
                long fmId = NFMStateHelper.getFamilyMemberIdFromStateCP();
                //update familymember record with received param "fmname"
                ContentValues v = new ContentValues(1);
                v.put(FamilyMemberContract.Columns.POSITION, getAiResult().getStringParameter("fmjob"));
                context.getContentResolver().update(FamilyMemberContract.CONTENT_URI, v, "_id=?",
                        new String[]{""+fmId});
                //TODO: this is the last step in new-family-member so clear the state record
                NFMStateHelper.finishFamilyMemberState();
            }
        });

        apiAiCommandMap.put("showBuildingInstruction", new SimpleReflexCommand() {
            @Override
            public void perform() {
                new BuildingInstructionHelper(context).showInstruction();
            }
        });

        apiAiCommandMap.put("sayIndoorLocation", new SimpleReflexCommand() {
            @Override
            public void perform() {
                new IndoorLocationHelper(context).sayLocation();
            }
        });

        apiAiCommandMap.put("executeStoredAlgorithm", new SimpleReflexCommand() {
            @Override
            public void perform() {
                new AlgorithmExecutor(context).findAndPerform(getAiResult().getStringParameter("algorithmName"));
            }
        });

        apiAiCommandMap.put("goToIndoorLocation", new SimpleReflexCommand() {
            @Override
            public void perform() {
                new IndoorLocationHelper(context).goToLocation(getAiResult().getStringParameter("indoorLocation"));
            }
        });
        //fighting
        apiAiCommandMap.put("fightStarted", new SimpleReflexCommand() {
            @Override
            public void perform() {
                //face to ninja
                Intent intent = new Intent("com.codegemz.elfi.coreapp.api.SET_EMOTION");
                intent.putExtra("emotion_type", EmojiType.Ninja.toString());
                intent.putExtra("emotion_level", (new EmojiManager().getMaxEmotionLevel(EmojiType.Ninja)));
                context.sendBroadcast(intent);
                //execute algorithm "stand"
                new AlgorithmExecutor(context).findAndPerform("stand");
            }
        });

        apiAiCommandMap.put("strikeCommand", new SimpleReflexCommand() {
            @Override
            public void perform() {
                String fightCommand = getAiResult().getStringParameter("Strike");
                if(fightCommand.equals("relax")){
                    Intent intent = new Intent("com.codegemz.elfi.coreapp.api.SET_EMOTION");
                    intent.putExtra("emotion_type", EmojiType.Happy.toString());
                    intent.putExtra("emotion_level", 0);
                    context.sendBroadcast(intent);
                    new AlgorithmExecutor(context).findAndPerform("relax");
                }
                else
                    new AlgorithmExecutor(context).findAndPerform(getAiResult().getStringParameter("Strike"));
            }
        });
        //programming
        apiAiCommandMap.put("programStart", new SimpleReflexCommand() {
            @Override
            public void perform() {
                ((BrainApp)context.getApplicationContext()).setRepeatProgramMap(new LinkedList<String>());
            }
        });

        apiAiCommandMap.put("repeatProgram", new SimpleReflexCommand() {
            @Override
            public void perform() {
                //AlgorithmExecutor ae = new AlgorithmExecutor(context);

                if(((BrainApp)context.getApplicationContext()).getRepeatProgramMap() == null)
                    return;

                Log.e("apiai:", "commands stored " + ((BrainApp)context.getApplicationContext()).getRepeatProgramMap().size());

                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        for(String command : ((BrainApp)context.getApplicationContext()).getRepeatProgramMap()) {
                            new AlgorithmExecutor(context).findAndPerform(command);
                            Log.e("apiai:", "command recovered " + command);
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

            }
        });

        apiAiCommandMap.put("movementAction", new SimpleReflexCommand() {
            @Override
            public void perform() {
                String programCommand = getAiResult().getStringParameter("Movement");
                new AlgorithmExecutor(context).findAndPerform(programCommand);

                if(((BrainApp)context.getApplicationContext()).getRepeatProgramMap() == null)
                    return;

                ((BrainApp)context.getApplicationContext()).getRepeatProgramMap().add(programCommand);
            }
        });


    }

    public boolean findMatchAndExecute(Result aiR) {
        String lastSentence = aiR.getAction();
        Log.e("apiai:", "DDD" + lastSentence);
        setAiResult(aiR);
        boolean result = false;
        for(String pattern : apiAiCommandMap.keySet()){
            if( lastSentence.equals(pattern) ) {
                apiAiCommandMap.get(pattern).perform();
                result = true;
            }
        }
        return result;
    }
}