package com.codegemz.elfi.coreapp.api.behavior_processor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SpeakBroadcastReceiver extends BroadcastReceiver {
    public SpeakBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, SpeakService.class);
        Log.e("SpeakBroadcastReceiver:", intent.getStringExtra("text"));
        service.putExtra("text", intent.getStringExtra("text"));
        context.startService(service);
    }
}
