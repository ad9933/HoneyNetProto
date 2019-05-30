package com.honeymade.honeynetproto;

import android.app.AppComponentFactory;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.net.*;

public class SocketCreate extends Thread {      //소켓 생성을 위한 스레드

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

        MainActivity.socket = socket;       //소켓 객체를 만들고 저장해준다.
    }


}
