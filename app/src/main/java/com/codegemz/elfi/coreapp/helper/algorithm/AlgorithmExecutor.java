package com.codegemz.elfi.coreapp.helper.algorithm;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.codegemz.elfi.apimanagers.AlgorithmDTO;
import com.codegemz.elfi.apimanagers.AlgorithmManager;
import com.codegemz.elfi.coreapp.BrainActivity;
import com.codegemz.elfi.coreapp.BrainApp;
import com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper.AlgorithmCompiler;
import com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper.Connector;

/**
 * Created by adrobnych on 9/10/15.
 */
public class AlgorithmExecutor {

    private Context context;
    private Connector connector;

    public AlgorithmExecutor(Context context) {
        this.context = context;
    }

    public void findAndPerform(String algorithmName) {
        AlgorithmDTO aDTO = new AlgorithmManager(context).getAlgorithmByName(algorithmName);
        if(aDTO != null){
            connector = ((BrainApp) context.getApplicationContext()).getConnector();
            if(connector.connect())
                try {
                    connector.writeCompositeMessage(new AlgorithmCompiler(context).compile(aDTO.get_id()));
                } catch (Exception e) {
                    Log.e("AlgorithmExecutor", "Exception: " + e);
                }
            else{
                Log.d("AlgExecutor", "BT connection failed");
            }
        }
        else {
            Log.e("AlgorithmExecutor", "Algorithm not found");
            if(!context.getClass().equals(com.codegemz.elfi.coreapp.api.ExtCommandIntentService.class))
                ((BrainActivity)context).getTthelper().speakText("I need more information.");
        }
    }

    public void findPerformAndWaitForResult(String algorithmName) {
        findAndPerform(algorithmName);
        if(connector.connect()) {
            new ReadMessageTask().execute("");
        }
    }

    private class ReadMessageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = connector.waitAndReadMessage();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("AlgorithmExecutor", "Successfully read message! received:" + result);
            if(result.contains("laser lost")) {
                Intent intent = new Intent("com.codegemz.elfi.coreapp.api.SPEAK");
                intent.putExtra("text", "Perimeter was crossed by unknown object. Alarm! All robots activated in defending mode!");
                context.sendBroadcast(intent);
            }
        }

    }
}
