package com.codegemz.elfi.coreapp.api;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.codegemz.elfi.coreapp.BrainApp;
import com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper.EV3BTConnector;
import com.codegemz.elfi.coreapp.helper.algorithm.AlgorithmExecutor;

import java.util.Set;

// used to integrate with VideoCommand Robot - Human telepresence
// TODO: make common place for BT initialization w/o duplication
public class ExtCommandIntentService extends IntentService {
    //private BTForBrain bt;
    public EV3BTConnector connector;
    private BluetoothAdapter bluetoothAdapter;
    private String btAddress;

    public ExtCommandIntentService() {
        super("ExtCommandIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initializeBT();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String extCommand = intent.getStringExtra("EXTERNAL_COMMAND");
            Log.e("ExtCommandService:", extCommand);

            //bt = new BTForBrain(this);
            //bt.initialize();
            if(((BrainApp)getApplicationContext().getApplicationContext()).getConnector() == null)
                initializeBT();

            //((BrainApp)getApplication()).getBrainActivity().getVrl().analyse(extCommand);
            new AlgorithmExecutor(ExtCommandIntentService.this).findAndPerform(extCommand);

            //((BrainApp) getApplicationContext()).setConnector(null);
        }
    }

    public void initializeBT(){
        Context context = ExtCommandIntentService.this;

        //create BT adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());

                SharedPreferences prefs = context.getSharedPreferences(
                        "com.codegemz.elfi.coreapp", Context.MODE_PRIVATE);
                String btNameKey = "com.codegemz.elfi.coreapp.bt_name";
                String btName = prefs.getString(btNameKey, "EV3");

                if(device.getName().equals(btName)) {
                    btAddress = device.getAddress();
                    Log.d("EV3BTConnector", "" + device.getAddress() + "  " + device.getName());
                    break;
                }
            }
        }

        //if(btAddress == null)
        //    showBTAlarm();

        // Register for broadcasts on BluetoothAdapter statechmange
        //IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        //context.registerReceiver(mReceiver, filter);

        // Establish a bluetooth connection to the NXT
        this.connector = new EV3BTConnector(context.getApplicationContext(), btAddress,
                bluetoothAdapter);

        ((BrainApp)context.getApplicationContext()).setConnector(this.connector);

        if(this.bluetoothAdapter.isEnabled() == false)
            this.connector.setConnectionAdapterState(true);
        else{
            Log.d("EV3BTConnector", "Bluetooth turned on");
        }
    }
}
