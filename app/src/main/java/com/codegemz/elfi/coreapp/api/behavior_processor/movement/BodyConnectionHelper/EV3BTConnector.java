package com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;


import org.apache.commons.lang3.ArrayUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by adrobnych on 4/4/15.
 */
public class EV3BTConnector implements Connector{
    public BluetoothAdapter bluetoothAdapter;
    public BluetoothSocket bluetoothSocket;
    public String address;

    private DataOutputStream outStream;

    Context ctx;

    @Override
    public String getTag(){
        return "EV3BTConnector";
    }

    @Override
    public String fieldDelimiter() {
        return "\r";
    }

    @Override
    public String commandDelimiter() {
        return "\r";
    }

    public EV3BTConnector(Context ctx, String address, BluetoothAdapter bluetoothAdapter) {
        this.address = address;
        this.bluetoothAdapter = bluetoothAdapter;

        this.ctx = ctx;
    }

    @Override
    public void setConnectionAdapterState(boolean state) {
        if(state == true) {
            // Check if bluetooth is off
            if(this.bluetoothAdapter.isEnabled() == false)
            {
                this.bluetoothAdapter.enable();

            }

        }
        // Check if bluetooth is enabled
        else if(state == false) {
            // Check if bluetooth is enabled
            if(this.bluetoothAdapter.isEnabled() == true)
            {
                this.bluetoothAdapter.disable();

            }

        }

    }

    @Override
    public boolean connect() {
        if(this.address == null)
            return false;
        if (outStream == null){
            boolean connected = false;
            BluetoothDevice nxt = this.bluetoothAdapter.getRemoteDevice(this.address);

            try {
                this.bluetoothSocket = nxt.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                this.bluetoothSocket.connect();
                connected = true;
                outStream = new DataOutputStream(this.bluetoothSocket.getOutputStream());
            } catch (IOException e) {
                connected = false;

            }

            return connected;
        }
        else
            return true;

    }

    @Override
    public void writeSingleMessage(String btCommand) {

        btMessage(btCommand);
    }

    private void btMessage(String btCommand) {

        if(this.bluetoothSocket!= null) {
            try{
                byte[] bt_message = null;
                switch(btCommand)
                {
                    case "motor_AB_FORWARD":
                        Log.e("connector", "goforward routine");
                        bt_message = new byte[]{(byte) 50, (byte) 0x00, (byte) 0x00, (byte) 0x00,

                            (byte) 0x80,
                            (byte) 0x08,
                            (byte) 0x00,

                            (byte) 0xC0, // opcode file related
                            (byte) 0x08,
                            (byte) 0x82,
                            (byte) 0x01, // program slot
                            (byte) 0x00,
                            (byte) 0x84, // data of string type 0 terminated


                            (byte) '.',
                            (byte) '.',
                            (byte) '/',
                            (byte) 'p',
                            (byte) 'r',
                            (byte) 'j',
                            (byte) 's',
                            (byte) '/',
                            (byte) 's',
                            (byte) 'a',
                            (byte) 'f',
                            (byte) 'e',
                            (byte) 'f',
                            (byte) 'o',
                            (byte) 'r',
                            (byte) 'w',
                            (byte) 'a',
                            (byte) 'r',
                            (byte) 'd',
                            (byte) '/',
                            (byte) 'P',
                            (byte) 'r',
                            (byte) 'o',
                            (byte) 'g',
                            (byte) 'r',

                            (byte) 'a',
                            (byte) 'm',

                            (byte) '.',
                            (byte) 'r',
                            (byte) 'b',
                            (byte) 'f',
                            (byte) 0x00,

                            (byte) 0x60,
                            (byte) 0x64,
                            (byte) 0x03,
                            (byte) 0x01,
                            (byte) 0x60,
                            (byte) 0x64,
                            (byte) 0x00

                        };
                        break;

                    case "motor_AB_LEFT":

                        bt_message = new byte[]{(byte) 28, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                            (byte) 0x80,
                            (byte) 0x00,
                            (byte) 0x00,

                            (byte) 0xA4, // set power command
                            (byte) 0x00, // layer ?
                            (byte) 0x01, // motor - A =1   B=2
                            (byte) 0x81,
                            (byte) (100),   // power              70,68
                            //   0 -> 32  33 <- 63    64 and 65 - max

                            (byte) 0xA4, // set power command
                            (byte) 0x00, // layer ?
                            (byte) 0x02, // motor - A =1   B=2
                            (byte) 0x81,
                            (byte) (-100),   // power
                            //   0 -> 32  33 <- 63   low | high  68 <- 85  85..120-silence

                            (byte) 0xA4, // set power command
                            (byte) 0x00, // layer ?
                            (byte) 0x04, // motor - A =1   B=2
                            (byte) 0x81,
                            (byte) (100),   // power              70,68
                            //   0 -> 32  33 <- 63    64 and 65 - max

                            (byte) 0xA4, // set power command
                            (byte) 0x00, // layer ?
                            (byte) 0x08, // motor - A =1   B=2
                            (byte) 0x81,
                            (byte) (-100),   // power

                            (byte) 0xA6,  //start motor command
                            (byte) 0,     // layer
                            (byte) 0x0f
                        };
                        break;

                    case "motor_AB_RIGHT":

                        bt_message = new byte[]{(byte) 28, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                            (byte) 0x80,
                            (byte) 0x00,
                            (byte) 0x00,

                            (byte) 0xA4, // set power command
                            (byte) 0x00, // layer ?
                            (byte) 0x01, // motor - A =1   B=2
                            (byte) 0x81,
                            (byte) (-100),   // power              70,68
                            //   0 -> 32  33 <- 63    64 and 65 - max

                            (byte) 0xA4, // set power command
                            (byte) 0x00, // layer ?
                            (byte) 0x02, // motor - A =1   B=2
                            (byte) 0x81,
                            (byte) (100),   // power
                            //   0 -> 32  33 <- 63   low | high  68 <- 85  85..120-silence

                            (byte) 0xA4, // set power command
                            (byte) 0x00, // layer ?
                            (byte) 0x04, // motor - A =1   B=2
                            (byte) 0x81,
                            (byte) (-100),   // power              70,68
                            //   0 -> 32  33 <- 63    64 and 65 - max

                            (byte) 0xA4, // set power command
                            (byte) 0x00, // layer ?
                            (byte) 0x08, // motor - A =1   B=2
                            (byte) 0x81,
                            (byte) (100),   // power
                            //   0 -> 32  33 <- 63   low | high  68 <- 85  85..120-silence


                            (byte) 0xA6,  //start motor command
                            (byte) 0,     // layer
                            (byte) 0x0f
                        };
                        break;
                    case "motor_AB_STOP":

                        bt_message = new byte[]{(byte) 0x09, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                            (byte) 0x80,
                            (byte) 0x00,
                            (byte) 0x00,

                            (byte) 0xA3,
                            (byte) 0x00,
                            (byte) 0x0f,    // A and B motors
                            (byte) 0x01    // 1 - tormoz
                        };
                        break;

                    case "motor_L1A_UP":
                        bt_message = new byte[]{(byte) 13, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                            (byte) 0x80,
                            (byte) 0x00,
                            (byte) 0x00,

                            (byte) 0xA4, // set power command
                            (byte) 0x01, // layer ?
                            (byte) 0x01, // motor - A =1   B=2
                            (byte) 0x81,
                            (byte) (-100),   // power              70,68

                            (byte) 0xA6,  //start motor command
                            (byte) 1,     // layer
                            (byte) 0x01
                        };
                        break;
                    case "motor_L1A_DOWN":

                        bt_message = new byte[]{(byte) 13, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                            (byte) 0x80,
                            (byte) 0x00,
                            (byte) 0x00,

                            (byte) 0xA4, // set power command
                            (byte) 0x01, // layer ?
                            (byte) 0x01, // motor - A =1   B=2
                            (byte) 0x81,
                            (byte) (100),   // power              70,68

                            (byte) 0xA6,  //start motor command
                            (byte) 1,     // layer
                            (byte) 0x01
                        };
                        break;
                    case "motor_L1A_STOP":

                        bt_message = new byte[]{(byte) 0x09, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                            (byte) 0x80,
                            (byte) 0x00,
                            (byte) 0x00,

                            (byte) 0xA3,
                            (byte) 0x01,
                            (byte) 0x01,    // A motor
                            (byte) 0x01    // 1 - tormoz
                        };
                        break;

                    case "motor_L1B_UP":
                        bt_message = new byte[]{(byte) 13, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00,

                                (byte) 0xA4, // set power command
                                (byte) 0x01, // layer ?
                                (byte) 0x02, // motor - A =1   B=2
                                (byte) 0x81,
                                (byte) (-20),   // power              70,68

                                (byte) 0xA6,  //start motor command
                                (byte) 1,     // layer
                                (byte) 0x02
                        };
                        break;
                    case "motor_L1B_DOWN":

                        bt_message = new byte[]{(byte) 13, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00,

                                (byte) 0xA4, // set power command
                                (byte) 0x01, // layer ?
                                (byte) 0x02, // motor - A =1   B=2
                                (byte) 0x81,
                                (byte) (100),   // power              70,68

                                (byte) 0xA6,  //start motor command
                                (byte) 1,     // layer
                                (byte) 0x02
                        };
                        break;
                    case "motor_L1B_STOP":

                        bt_message = new byte[]{(byte) 0x09, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00,

                                (byte) 0xA3,
                                (byte) 0x01,
                                (byte) 0x02,    // A motor
                                (byte) 0x01    // 1 - tormoz
                        };
                        break;

                    case "motor_L1C_UP":
                        bt_message = new byte[]{(byte) 13, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00,

                                (byte) 0xA4, // set power command
                                (byte) 0x01, // layer ?
                                (byte) 0x04, // motor - A =1   B=2
                                (byte) 0x81,
                                (byte) (-20),
                                (byte) 0xA6,  //start motor command
                                (byte) 1,     // layer
                                (byte) 0x04
                        };
                        break;
                    case "motor_L1C_DOWN":

                        bt_message = new byte[]{(byte) 13, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00,

                                (byte) 0xA4, // set power command
                                (byte) 0x01, // layer ?
                                (byte) 0x04, // motor - A =1   B=2
                                (byte) 0x81,
                                (byte) (100),   // power              70,68

                                (byte) 0xA6,  //start motor command
                                (byte) 1,     // layer
                                (byte) 0x04
                        };
                        break;
                    case "motor_L1C_STOP":

                        bt_message = new byte[]{(byte) 0x09, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00,

                                (byte) 0xA3,
                                (byte) 0x01,
                                (byte) 0x04,    // A motor
                                (byte) 0x01    // 1 - tormoz
                        };
                        break;

                    case "motor_L1D_UP":
                        bt_message = new byte[]{(byte) 13, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00,

                                (byte) 0xA4, // set power command
                                (byte) 0x01, // layer ?
                                (byte) 0x08, // motor - A =1   B=2
                                (byte) 0x81,
                                (byte) (40),
                                (byte) 0xA6,  //start motor command
                                (byte) 1,     // layer
                                (byte) 0x08
                        };
                        break;
                    case "motor_L1D_DOWN":

                        bt_message = new byte[]{(byte) 13, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00,

                                (byte) 0xA4, // set power command
                                (byte) 0x01, // layer ?
                                (byte) 0x08, // motor - A =1   B=2
                                (byte) 0x81,
                                (byte) (-40),   // power              70,68

                                (byte) 0xA6,  //start motor command
                                (byte) 1,     // layer
                                (byte) 0x08
                        };
                        break;
                    case "motor_L1D_STOP":

                        bt_message = new byte[]{(byte) 0x09, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00,

                                (byte) 0xA3,
                                (byte) 0x01,
                                (byte) 0x08,    // A motor
                                (byte) 0x01    // 1 - tormoz
                        };
                        break;
                    case "motor_Arduino_FORWARD":

                        bt_message = new byte[]{(byte) 'F'};
                        break;
                    case "motor_Arduino_LEFT":

                        bt_message = new byte[]{(byte) 'L'};
                        break;
                    case "motor_Arduino_RIGHT":

                        bt_message = new byte[]{(byte) 'R'};
                        break;
                    case "pesik_left_step":
                        bt_message = new byte[]{(byte) 18, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00,

                                (byte) 0xA4, // set power command
                                (byte) 0x00, // layer ?
                                (byte) 0x01, // motor - A =1   B=2
                                (byte) 0x81,
                                (byte) (100),   // power              70,68
                                //   0 -> 32  33 <- 63    64 and 65 - max

                                (byte) 0xA4, // set power command
                                (byte) 0x00, // layer ?
                                (byte) 0x02, // motor - A =1   B=2
                                (byte) 0x81,
                                (byte) (-100),   // power
                                //   0 -> 32  33 <- 63   low | high  68 <- 85  85..120-silence

                                (byte) 0xA6,  //start motor command
                                (byte) 0,     // layer
                                (byte) 0x0f
                        };
                        break;
                    case "pesik_right_step":

                        bt_message = new byte[]{(byte) 18, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00,

                                (byte) 0xA4, // set power command
                                (byte) 0x00, // layer ?
                                (byte) 0x01, // motor - A =1   B=2
                                (byte) 0x81,
                                (byte) (-100),   // power              70,68
                                //   0 -> 32  33 <- 63    64 and 65 - max

                                (byte) 0xA4, // set power command
                                (byte) 0x00, // layer ?
                                (byte) 0x02, // motor - A =1   B=2
                                (byte) 0x81,
                                (byte) (100),   // power
                                //   0 -> 32  33 <- 63   low | high  68 <- 85  85..120-silence

                                (byte) 0xA6,  //start motor command
                                (byte) 0,     // layer
                                (byte) 0x0f
                        };
                        break;
                    case "pesik_go":

                        bt_message = new byte[]{(byte) 18, (byte) 0x00, (byte) 0x80, (byte) 0x00,
                                (byte) 0x80,
                                (byte) 0x00,
                                (byte) 0x00,

                                (byte) 0xA4, // set power command
                                (byte) 0x00, // layer ?
                                (byte) 0x01, // motor - A =1   B=2
                                (byte) 0x81,
                                (byte) (-100),   // power              70,68
                                //   0 -> 32  33 <- 63    64 and 65 - max

                                (byte) 0xA4, // set power command
                                (byte) 0x00, // layer ?
                                (byte) 0x02, // motor - A =1   B=2
                                (byte) 0x81,
                                (byte) (-100),   // power
                                //   0 -> 32  33 <- 63   low | high  68 <- 85  85..120-silence

                                (byte) 0xA6,  //start motor command
                                (byte) 0,     // layer
                                (byte) 0x0f
                        };

                }

                outStream.write(bt_message, 0, bt_message.length);
                Log.e(getTag(), "Successfully written message" + new String(bt_message));

            }
            catch (Exception e) {

                Log.e(getTag(), "Couldn't write message: " + e);

            }


        }
        else {
            Log.d(getTag(), "Couldn't write message");
        }
    }

    @Override
    public void writeCompositeMessage(String compiledAlgorithm) {

        if(this.bluetoothSocket!= null) {
            try{
                //EV3 uses \r instead of \n in files!!!!!
                byte[] bt_message = compose_algorithm_as_a_single_BT_message(
                        //"77\r0\r91\r0\r49\r0\r77\r180\r90\r7\r77\r90\r91\r0\r49\r0\r77\r180\r90\r2\r77\r90\r90\r6\r77\r0\r90\r12\r77\r-90\r49\r0\r22\r0\r"
                    compiledAlgorithm
                    );

                outStream.write(bt_message, 0, bt_message.length);
                Log.e(getTag(), "Successfully written message" + new String(bt_message));

            }
            catch (Exception e) {

                Log.e(getTag(), "Couldn't write message: " + e);

            }


        }
        else {
            Log.d(getTag(), "Couldn't write message");
        }
    }

    private byte[] compose_algorithm_as_a_single_BT_message(String algorithmBody) {


        byte[] bt_message = new byte[14 + algorithmBody.length()];
        bt_message[0] = (byte) (12 + algorithmBody.length());
        bt_message[1] = (byte) 0x00;

        bt_message[2] = (byte) 1;
        bt_message[3] = (byte) 0x00;

        bt_message[4] = (byte) 0x81; // opcode file related
        bt_message[5] = (byte) 0x9E;
        bt_message[6] = (byte) 0x04; // mailbox name length
        bt_message[7] = (byte) 'a'; // mailbox name
        bt_message[8] = (byte) 'b';
        bt_message[9] = (byte) 'c';
        bt_message[10] = (byte) 0x00;

        bt_message[11] = (byte) (algorithmBody.length() + 1);
        bt_message[12] = (byte) 0x00;


        for (int i = 0; i < algorithmBody.length(); i++)
            bt_message[13 + i] = (byte) algorithmBody.charAt(i);

        bt_message[14 + algorithmBody.length() - 1] = (byte) 0x00;

        return bt_message;
    }

    @Override
    public void close() {

        try {
            //outStream.flush();
            if(outStream != null)
                outStream.close();
        } catch (IOException e) {
            Log.d(getTag(), "Couldn't close BT connector: " + e);
        }
    }

    private DataInputStream input;

    @Override
    public String waitAndReadMessage() {
        String message = "error, empty string";

        if(this.bluetoothSocket!= null) {
            try {
                Log.e("connector:", "started listening");
                byte[] buffer = new byte[500];
                input = new DataInputStream(this.bluetoothSocket.getInputStream());
                int length = input.read(buffer);

                message = "" + (new String(buffer)) + ": length: " + length + "\n";
                for(int i=0; i< length; i++)
                    message = message + "|" + ((int)buffer[i]&0xff);
                Log.d(getTag(), "Successfully read message");
                //Toast.makeText(ctx, "Successfully read message!", Toast.LENGTH_LONG).show();
                Log.e("connector:", "finished listening");
                //input.close();

                //outStream.close();



            }
            catch (IOException e) {
                message = e.toString();
                Log.d(getTag(), "Couldn't read message ..");
                //Toast.makeText(ctx, "Couldn't read message!", Toast.LENGTH_LONG).show();

            }
        }
        else {
            message = "bluetoothSocket == null";
            Log.d(getTag(), "Couldn't read message ..........");
            //Toast.makeText(ctx, "Couldn't read message", Toast.LENGTH_LONG).show();

        }

        return message;

    }
}