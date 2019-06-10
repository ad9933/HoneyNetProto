package com.honeymade.honeynetproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class DeepLinkActivity extends AppCompatActivity {       //이용자가 다른 어플을 이용하다가 딥링크를 열었을 경우 시작되는 액티비티이다.

    private final String DEEPLINK_DATA = "http://honey.app/12345";  //딥링크로 어떤 주소값에 반응할지를 설정한다.
    private final String DEEPLINK_DATA_DOOR = "http://honey.app/door";
    private final String ARDUINO_BT_BUZZER= "http://honey.app/buzzer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {        //이용자가 딥링크를 열었을 경우 액티비티가 시작되면서 함수가 실행됨
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);

        if (MainActivity.socket == null) {                      //MainActivity 가 아직 실행되지 않아 소켓 생성이 안되었을때를 대비하여
                                                                //소켓이 생성되지 않았으면 소켓을 생성해 준다.
            SocketCreate sc = new SocketCreate();
            sc.start();
        }

        onNewIntent(getIntent());

    }

    protected void onNewIntent(Intent intent) {                 //딥링크를 처리하는 함수이다.
        String action = intent.getAction();
        String data = intent.getDataString();                   //딥링크의 데이터를 읽은 후 데이터를 통해서 해야하는 일을 구분한다.

        System.out.println("action : " + action);               //디버그를 위해서 딥링크 데이터를 남긴다.
        System.out.println("data : " + data);

        if (data.equals(DEEPLINK_DATA)) {                       //딥링크 데이터를 비교하는 과정
            SendThread st = new SendThread(MainActivity.socket, MainActivity.HEADER_LED_ON.getBytes());   //만약 정보를 보내라는 데이터일 경우
            st.start();                                                                     //소켓으로 정보를 전송한다.
        } else if (data.equals(ARDUINO_BT_BUZZER)) {

            try {
                MainActivity.btOut.write(123);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (data.equals(DEEPLINK_DATA_DOOR)) {
            SendThread st = new SendThread(MainActivity.socket, MainActivity.HEADER_DOOR_OPEN.getBytes());
            st.start();
        }

    }
}
