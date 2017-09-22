package com.codegemz.elfi.coreapp.helper.indoor_location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;

import com.codegemz.elfi.coreapp.BrainActivity;

import java.util.List;

/**
 * Created by adrobnych on 9/22/15.
 */
public class WifiScanReceiver extends BroadcastReceiver {
    private WifiObserver wifiObserver;

    public WifiScanReceiver(WifiObserver wifiObserver){
        this.wifiObserver = wifiObserver;
    }

    public void onReceive(Context c, Intent intent) {
        List<ScanResult> wifiScanList = ((BrainActivity)c).getWifi().getScanResults();
        String[] wifis = new String[wifiScanList.size()];

        int maxpower = -1000;
        String maxWifi = "";
        for(int i = 0; i < wifiScanList.size(); i++){
            wifis[i] = ((wifiScanList.get(i)).toString());
            if(wifiScanList.get(i).level>maxpower) {
                maxpower = wifiScanList.get(i).level;
                maxWifi = wifiScanList.get(i).SSID;
            }
        }
        wifiObserver.inform(maxWifi);
    }
}