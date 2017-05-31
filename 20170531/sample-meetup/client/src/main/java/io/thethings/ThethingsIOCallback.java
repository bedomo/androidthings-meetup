package io.thethings;

/**
 * Created by user on 5/24/17.
 */

public interface ThethingsIOCallback {
    void receivePayload(String payload);
    void lostConnection(String exception);
    void deliveryMessage(String token);

}
