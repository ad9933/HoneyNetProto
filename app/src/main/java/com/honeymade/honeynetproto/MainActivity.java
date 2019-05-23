package com.honeymade.honeynetproto;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.icu.util.Output;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static InetAddress DESTINATION;
    public static final int PORT = 7777;        //UDP통신에 사용될 포트
    public static final int REQUEST_ENABLE_BT = 10;

    public static final String BT_NAME = "HONEY";    //AT Command로 설정된 아두이노 블루투스 모듈의 이름
    public static BluetoothDevice btDevice = null;
                                                    //패킷의 맨 앞에 붙을 헤더, 아두이노에게 무엇을 할지를 알려주는 역할을 함
    public static final byte HEADER_MSG = 0x10;     //LCD에 표시할 메시지를 의미함
    public static final byte HEADER_CAM = 0x11;     //카메라를 작동시키라는 명령을 의미함
    public static final byte HEADER_LED = 0x12;     //불을 켜라는 명령을 의미함

    public static DatagramSocket socket = null;
    public static BluetoothAdapter btAdapter = null;
    public static BluetoothSocket btSocket = null;

    public static InputStream btIn = null;
    public static OutputStream btOut = null;

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
            System.out.println("[HONEY] BT START");
            //블루투스가 꺼져있을경우 이용자에게 블루투스를 켜도록 요청함
            if(!btAdapter.isEnabled()) {
                Intent enableBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBt, REQUEST_ENABLE_BT);
            }

            Set<BluetoothDevice> pairedDevice = btAdapter.getBondedDevices();   //페어링된 기기들을 불러옴

            for (BluetoothDevice device : pairedDevice) {   //페어링된 기기중에 우리가 원하는 주소가 있는지 확인함
                if (device.getName().equals(BT_NAME)) {
                    btDevice = device;
                    found = true;
                }
            }

            //페어링된 기기중에서 우리가 원하는 블루투스 모듈을 찾지 못했을경우 오류 메세지를 출력함
            if (!found) {
                System.out.println("[HONEY] BT NOT FOUND!!");
            } else {        //연결된 아두이노 블루투스 모듈을 발견했을 때 실행하는 구문
                System.out.println("[HONEY] BT FOUND!!");
                try {       //발견했을 경우 연결을 시도하며 블루투스 소켓을 만듦
                    btSocket = btDevice.createRfcommSocketToServiceRecord(java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
                    btSocket.connect();
                    System.out.println("[HONEY] BT CONNECTED!!");
                    btOut = btSocket.getOutputStream();
                    System.out.println("[HONEY] BT OUTPUT STREAM INITIATED!!");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }
        //블루투스 미지원 기기일때 실행됨
        if(btAdapter == null) {System.out.println("[HONEY] ERROR");}

    }

    public void onSendBtBtn(View v) {
        //블루투스로 메세지를 보내는 버튼이 눌렸을때 실행되는 부분
        if(btOut != null) {
            try {
                btOut.write(123);   //정수 123을 블루투스를 통하여 송신한다.
                System.out.println("[HONEY] BT MSG SENT!!");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("[HONEY] BT OUTPUT STREAM ERROR");
        }

    }

    public void onSendBtn(View v) {     //만들어진 소켓을 통해서 아두이노로 정보를 전송함.
                                        //정보를 전송한 것을 아두이노에서 읽음으로서 여러가지 작업을 할 수 있음
        SendThread thread = new SendThread(socket, "HONEY".getBytes());     //정보를 보내는 과정도 스레드로 다룬다
        thread.start();
        System.out.println("Message Thread Loaded!");       //스레드가 켜졌음을 로그로 표시해줌

    }

    public void onMsgSendBtn(View v) {  //LCD에 표시할 메시지를 전송함
        String msg = null;
        EditText textInput = (EditText)findViewById(R.id.msgbox);
        msg = textInput.getText().toString();   //사용자가 텍스트 상자에 입력한 메시지를 아두이노로 보냄
        textInput.setText("");
        ((TextView)findViewById(R.id.input_viewer)).setText(msg);

        SendThread thread = new SendThread(socket, (HEADER_MSG + msg).getBytes());
        thread.start();

    }
}
