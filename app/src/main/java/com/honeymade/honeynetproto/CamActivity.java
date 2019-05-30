package com.honeymade.honeynetproto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        if(MainActivity.socket == null) {           //메인액티비티가 켜지지 않았을때를 대비
            SocketCreate sc = new SocketCreate();
            sc.start();
        }

        SendThread camSender = new SendThread(MainActivity.socket, MainActivity.HEADER_CAM);
        camSender.start();

        ReceiveThread camDataReceiver = new ReceiveThread(MainActivity.socket);

    }
}