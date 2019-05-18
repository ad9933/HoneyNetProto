package com.honeymade.honeynetproto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    public static InetAddress DESTINATION;
    public static final int PORT = 7777;

    DatagramSocket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            DESTINATION = InetAddress.getByName("192.168.0.10");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        SocketCreate sc = new SocketCreate(this);
        sc.start();

    }

    public void onSendBtn(View v) {
        SendThread thread = new SendThread(socket, "HONEY".getBytes());
        thread.start();
        System.out.println("Message Thread Loaded!");


    }

}
