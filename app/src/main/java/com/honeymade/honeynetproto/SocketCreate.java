package com.honeymade.honeynetproto;

import android.app.AppComponentFactory;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.net.*;

public class SocketCreate extends Thread {

    DatagramSocket socket;
    MainActivity mainActivity;

    SocketCreate(MainActivity mainActivity) {
        socket = mainActivity.socket;
        this.mainActivity = mainActivity;

        try  {
            socket = new DatagramSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        try  {
            socket = new DatagramSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainActivity.socket = socket;
    }


}
