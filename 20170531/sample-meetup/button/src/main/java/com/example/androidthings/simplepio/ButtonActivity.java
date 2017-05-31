/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.simplepio;

import android.app.Activity;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

import io.thethings.ThethingsIOCallback;
import io.thethings.ThethingsIOClient;

/**
 * Sample usage of the Gpio API that logs when a button is pressed.
 *
 */
public class ButtonActivity extends Activity implements ThethingsIOCallback {
    private static final String TAG = ButtonActivity.class.getSimpleName();

    String thingToken = "uq5hJlKZxikCaCa2LldhSmQ_nziHsqVSoe78Tfq8l2s";

    private boolean mLedState = false;

    private Gpio mButtonGpio;

    private ThethingsIOClient mClient;

    private String message = "{ \"values\" : [{ \"key\" : \"light\", \"value\" : \"";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting ButtonActivity");

        mClient = new ThethingsIOClient();
        mClient.setCallback(this);
        mClient.connectToThingsIO(thingToken);

        PeripheralManagerService service = new PeripheralManagerService();
        try {
            String pinName = BoardDefaults.getGPIOForButton();
            mButtonGpio = service.openGpio(pinName);
            mButtonGpio.setDirection(Gpio.DIRECTION_IN);
            mButtonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);
            mButtonGpio.registerGpioCallback(new GpioCallback() {
                @Override
                public boolean onGpioEdge(Gpio gpio) {
                    Log.i(TAG, "GPIO changed, button pressed");
                    mLedState = !mLedState;
                    int value = 0;
                    if (mLedState)
                    {
                        value = 1;
                    }
                    String mqtt_message = message + value +  "\" } ]  }";
                    mClient.sendToThingsIO(thingToken,  mqtt_message);
                    // Return true to continue listening to events
                    return true;
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mButtonGpio != null) {
            // Close the Gpio pin
            Log.i(TAG, "Closing Button GPIO pin");
            try {
                mButtonGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            } finally {
                mButtonGpio = null;
            }
        }
    }

    @Override
    public void receivePayload(String payload) {

    }

    @Override
    public void lostConnection(String exception) {

    }

    @Override
    public void deliveryMessage(String token) {

    }
}
