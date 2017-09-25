package com.codegemz.elfi.coreapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

public class ShowMapReceiver extends BroadcastReceiver {
    public static final String SHOW_MAP_ACTION = "com.elfirobotics.beehive.sense_intents.SHOP.MAP_TO_SHOW";

    public ShowMapReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action == SHOW_MAP_ACTION) {
            String image_url = intent.getStringExtra("com.elfirobotics.beehive.sense_intents.extra.IMAGE_URL");
            if (image_url != null) {

                ImageView drawerImage =
                        ((BrainApp)context.getApplicationContext()).getBrainActivity().getIv();

                Ion.with(drawerImage)
                        //.placeholder(R.drawable.elfixicon)
                        .error(R.drawable.new_emo_static_sad)
                        //.animateLoad(spinAnimation)
                        //.animateIn(fadeInAnimation)
                        .load(image_url);
            }
            Log.e("ShowMapReceiver", "Show Map Intent received: " + image_url);
        }
    }
}
