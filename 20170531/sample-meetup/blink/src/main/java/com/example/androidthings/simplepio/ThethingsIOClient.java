package com.example.androidthings.simplepio;

import android.os.StrictMode;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by user on 5/24/17.
 */

public class ThethingsIOClient implements MqttCallback {

    MqttClient client;

    ThethingsIOCallback callback;

    private String serverIp =  "tcp://mqtt.thethings.io:1883"; // mqtt.thethings.io"

    private String serverPath = "v2/things/";

    // get from the board
    private String token = "";

    public ThethingsIOClient(String thingToken)
    {
        this.token = thingToken;
    }

    public void setCallback(ThethingsIOCallback callback)
    {
        this.callback = callback;
    }

    public void connect(String clientId, String topic)
    {
        try {
            //String mqttURI = serverDomain + serverPath + token;


            client = new MqttClient(serverIp, "", new MemoryPersistence());
            client.setCallback(this);
            client.connect();

            //String topic = "topic/led";
            client.subscribe(serverPath + token);

        }
        catch (MqttException e) {
            e.printStackTrace();
        }


    }

    public void close()
    {

    }


    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
            if (callback != null)
            {
                callback.receivePayload(new String(message.getPayload()));
            }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
