package com.honeymade.honeynetproto;

import java.io.IOException;
import java.net.*;

public class SendThread extends Thread {    //정보를 전송하는 스레드

    DatagramSocket socket;
    byte[] data = null;

    SendThread(DatagramSocket socket, byte[] data) {    //생성된 소켓과 데이터를 생성자의 인자로 넘겨준다.
        this.data = data;
        this.socket = socket;
    }

    @Override
    public void run() {     //패킷을 생성하고 MainActivity에 설정된 아두이노의 주소로 정보를 전송한다
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