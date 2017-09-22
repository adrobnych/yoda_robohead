package com.codegemz.elfi.coreapp.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ExternalCommandReceiver extends BroadcastReceiver {
    public ExternalCommandReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String externalCommand = intent.getStringExtra("EXTERNAL_COMMAND");
        Log.e("ExtCommandReceiver:", externalCommand);

        // start service to do heavy work
        Intent iService = new Intent(context, ExtCommandIntentService.class);
        iService.putExtra("EXTERNAL_COMMAND",externalCommand);
        context.startService(iService);
    }
}
