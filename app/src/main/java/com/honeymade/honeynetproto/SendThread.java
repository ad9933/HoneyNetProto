package com.honeymade.honeynetproto;

import java.io.IOException;
import java.net.*;

public class SendThread extends Thread {

    DatagramSocket socket;
    byte[] data = null;

    SendThread(DatagramSocket socket, byte[] data) {
        this.data = data;
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();

        DatagramPacket dp = new DatagramPacket(data, data.length, MainActivity.DESTINATION, MainActivity.PORT);
        try {
            socket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Message Sent!");
    }
}