package com.honeymade.honeynetproto;

import java.io.IOException;
import java.net.*;

public class ReceiveThread extends Thread {

    DatagramSocket socket = null;
    DatagramPacket input = null;

    MessageDecoder md;

    public ReceiveThread(DatagramSocket socket) {
        super();

        this.socket = socket;

    }

    public void run() {
        try {
            socket.receive(input);
        } catch (IOException e) {e.printStackTrace();}

        if (input.getAddress().equals(MainActivity.DESTINATION)) {
            md = new MessageDecoder(input.getData());
        }

    }

    public int[][] getRGB() { return md.getRGB(); }

}
