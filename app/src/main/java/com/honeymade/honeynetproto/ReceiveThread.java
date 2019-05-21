package com.honeymade.honeynetproto;

import java.io.IOException;
import java.net.*;

public class ReceiveThread extends Thread {     //아두이노에서 보낸 정보를 받는 스레드

    DatagramSocket socket = null;
    DatagramPacket input = null;

    MessageDecoder md;  //사진정보를 처리하기 위해서 MessageDecoder클래스를 사용한다.

    public ReceiveThread(DatagramSocket socket) {
        super();

        this.socket = socket;

    }

    public void run() {     //실행하면 소켓에서 정보를 받고 이를 이미지 디코더에 전달한다.
        try {
            socket.receive(input);
        } catch (IOException e) {e.printStackTrace();}

        if (input.getAddress().equals(MainActivity.DESTINATION)) {
            md = new MessageDecoder(input.getData());
        }

    }

    public int[][] getRGB() { return md.getRGB(); }

}
