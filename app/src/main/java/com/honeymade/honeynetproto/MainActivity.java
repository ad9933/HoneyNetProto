package com.honeymade.honeynetproto;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.*;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static InetAddress DESTINATION;
    public static final int PORT = 7777;        //UDP통신에 사용될 포트
    public static final int REQUEST_ENABLE_BT = 10;

    public static final String BT_ADDR = "";    //아두이노 블루투스모듈의 MAC주소
    public static BluetoothDevice btDevice = null;

    public static DatagramSocket socket = null;
    public static BluetoothAdapter btAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {        //어플을 열었을때 시작되는 함수
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            DESTINATION = InetAddress.getByName("192.168.0.10");    //아두이노의 LAN 주소를 등록
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //UDP통신 소켓 생성을 위한 스레드 생성
        //안드로이드에서는 소켓과 관련된 작업을 하기 위해서는 스레드를 따로 생성하여 다뤄야함
        SocketCreate sc = new SocketCreate();
        sc.start();

    }

    public void onBtStart(View v) {     //START BT 버튼을 눌렀을때 실행됨
        boolean found = false;
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        //기기가 블루투스를 지원하는지 확인
        if(btAdapter != null) {
            //블루투스가 꺼져있을경우 이용자에게 블루투스를 켜도록 요청함
            if(!btAdapter.isEnabled()) {
                Intent enableBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBt, REQUEST_ENABLE_BT);
            }

            Set<BluetoothDevice> pairedDevice = btAdapter.getBondedDevices();   //페어링된 기기들을 불러옴

            for (BluetoothDevice device : pairedDevice) {   //페어링된 기기중에 우리가 원하는 주소가 있는지 확인함
                if (device.getAddress().equals(BT_ADDR)) {
                    btDevice = device;
                    found = true;
                }
            }

            //페어링된 기기중에서 우리가 원하는 블루투스 모듈을 찾지 못했을경우 오류 메세지를 출력함
            if (!found) {
                Toast notFound = Toast.makeText(this, "Not Paired!!", Toast.LENGTH_SHORT);
                notFound.show();
            }

        }

    }

    public void onSendBtn(View v) {     //만들어진 소켓을 통해서 아두이노로 정보를 전송함.
                                        //정보를 전송한 것을 아두이노에서 읽음으로서 여러가지 작업을 할 수 있음
        SendThread thread = new SendThread(socket, "HONEY".getBytes());     //정보를 보내는 과정도 스레드로 다룬다
        thread.start();
        System.out.println("Message Thread Loaded!");       //스레드가 켜졌음을 로그로 표시해줌

    }

}
