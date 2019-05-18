package com.honeymade.honeynetproto;

import android.app.AppComponentFactory;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.net.*;

public class SocketCreate extends Thread {

    DatagramSocket socket;

    SocketCreate() {
        super();
    }

    @Override
    public void run() {
        super.run();
        try  {
            socket = new DatagramSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainActivity.socket = socket;
    }


}
