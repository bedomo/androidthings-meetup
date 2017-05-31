package com.example.androidthings.simplepio;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by user on 5/24/17.
 */

public class ThethingsIOClient implements MqttCallback {

    MqttClient client;

    ThethingsIOCallback callback;

    private String serverDomain =  "mqtt.thethings.io";

    public ThethingsIOClient()
    {

    }

    public void setCallback(ThethingsIOCallback callback)
    {
        this.callback = callback;
    }

    public void connect(String serverIp, String clientId, String topic)
    {
        try {
            client = new MqttClient(serverIp, clientId, new MemoryPersistence());
            client.setCallback(this);
            client.connect();

            //String topic = "topic/led";
            client.subscribe(topic);

        } catch (MqttException e) {
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
