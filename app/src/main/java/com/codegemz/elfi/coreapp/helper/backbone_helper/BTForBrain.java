package com.codegemz.elfi.coreapp.helper.backbone_helper;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.codegemz.elfi.coreapp.BrainApp;
import com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper.Connector;
import com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper.EV3BTConnector;

import java.util.Set;

/**
 * Created by adrobnych on 6/22/15.
 */
public class BTForBrain {

    private Context context;
    public Connector connector;
    private BluetoothAdapter bluetoothAdapter;
    private String btAddress;

    public BTForBrain(Context context) {
        this.context = context;
    }

    public void initialize(){
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
                    Log.d("BTConnector", "" + device.getAddress() + "  " + device.getName());
                    break;
                }
            }
        }

        //if(btAddress == null)
        //    showBTAlarm();

        // Register for broadcasts on BluetoothAdapter statechmange
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(mReceiver, filter);

        // TODO: make variation EV# vs Arduino
        this.connector = new EV3BTConnector(context.getApplicationContext(), btAddress,
                bluetoothAdapter);

        ((BrainApp)context.getApplicationContext()).setConnector(this.connector);

        if(this.bluetoothAdapter.isEnabled() == false)
            this.connector.setConnectionAdapterState(true);
        else{
            Log.d(connector.getTag(), "Bluetooth turned on");
        }
    }

    private void showBTAlarm() {
        new AlertDialog.Builder(context)
                .setTitle("No EV3 devices found!")
                .setMessage("Please pair with one and only one EV3 device using Bluetooth settings on this device")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();
    }


    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d("BTConnector", "Bluetooth turned off");
                        Toast.makeText(context.getApplicationContext(), "Bluetooth turned off!", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:

                        break;
                    case BluetoothAdapter.STATE_ON:

                        Log.d("BTConnector", "Bluetooth turned on");
                        Toast.makeText(context.getApplicationContext(), "Bluetooth turned on!", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:

                        break;
                }
            }
        }
    };

}
