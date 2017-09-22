package com.codegemz.elfi.coreapp.helper.indoor_location;

import android.content.Context;

import com.codegemz.elfi.apimanagers.AlgorithmDTO;
import com.codegemz.elfi.apimanagers.AlgorithmManager;
import com.codegemz.elfi.apimanagers.IndoorLocationDTO;
import com.codegemz.elfi.apimanagers.IndoorLocationManager;
import com.codegemz.elfi.coreapp.BrainActivity;
import com.codegemz.elfi.coreapp.helper.algorithm.AlgorithmExecutor;
import com.codegemz.elfi.model.IndoorLocation;

/**
 * Created by adrobnych on 8/30/15.
 */
public class IndoorLocationHelper {
    private Context  ctx;
    public IndoorLocationHelper(Context ctx){
        this.ctx = ctx;
    }

    public void sayLocation() {
        ((BrainActivity)ctx).
                startWifiScannerAndNotify(new WifiObserver() {
                                              @Override
                                              public void inform(String wifiName) {
                                                  ((BrainActivity) ctx).getTthelper().speakText(
                                                          "I am in " + (new IndoorLocationManager(ctx)).getLocationNameByWifi(wifiName));
                                                  ctx.unregisterReceiver(
                                                          ((BrainActivity) ctx).getWifiReciever());
                                              }
                                          }
                );
    }

    public void goToLocation(final String destination) {
        ((BrainActivity)ctx).
                startWifiScannerAndNotify(new WifiObserver() {
                                              @Override
                                              public void inform(String wifiName) {
                                                  goToLocationFromTo(
                                                          (new IndoorLocationManager(ctx)).getLocationNameByWifi(wifiName),
                                                          destination);
                                                  ctx.unregisterReceiver(
                                                          ((BrainActivity) ctx).getWifiReciever());
                                              }
                                          }
                );
    }

    private void goToLocationFromTo(String from, String to) {
        AlgorithmDTO foundAlgorithm = new AlgorithmManager(ctx).findAlgorithmByV1AndV2(from, to);
        if(foundAlgorithm != null)
            new AlgorithmExecutor(ctx).findAndPerform(foundAlgorithm.getName());
    }
}
